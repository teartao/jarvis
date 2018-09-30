package com.demon.cache;

import com.demon.bean.FactoryList;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AopCacheOperationDelegate {

    private Delegate directDelegate = new DirectDelegate();

    private Delegate multipleDelegate = new MultipleDelegate();

    @Resource
    private FactoryList<Media, Cache<CacheKey, Object>> cacheFactory;

    @Resource
    private CacheKeyRedisContainer cacheKeyRedisContainer;

    public Object cached(Detector detector, ProceedingJoinPoint point) throws MethodInvokeException {
        return (detector.multiple()
                ? multipleDelegate
                : directDelegate)
                .apply(detector, cacheFactory.getBean(detector.cached().media()), point);
    }

    public void remove(String id, Media media) {
        if (!StringUtils.hasText(id)) {
            throw new CacheException("CachedRemove的id不能为空");
        }
        Cache<CacheKey, Object> cache = cacheFactory.getBean(media);
        if (cache.keyStore()) {
            cacheKeyRedisContainer.get(id)
                    .stream()
                    .map(key -> new CacheKey(key, id))
                    .forEach(cache::remove);
            cacheKeyRedisContainer.clear(id);
        } else {
            cache.remove(new CacheKey(null, id));
        }
    }

    interface Delegate {
        Object apply(Detector detector, Cache<CacheKey, Object> cache, ProceedingJoinPoint point) throws MethodInvokeException;
    }

    class DirectDelegate implements Delegate {

        @Override
        public Object apply(Detector detector, Cache<CacheKey, Object> cache, ProceedingJoinPoint point) throws MethodInvokeException {
            CacheKey cacheKey = detector.cacheKeys().get(0);
            Cached cached = detector.cached();
            Object cacheValue = cache.get(cacheKey);
            if (cacheValue == null) {
                try {
                    cacheValue = point.proceed();
                } catch (Throwable e) {
                    throw new MethodInvokeException(e);
                }
                if (cacheValue != null) {
                    cache.put(cacheKey, cacheValue, Condition.of(cached));
                    if (cache.keyStore()) {
                        cacheKeyRedisContainer.push(cacheKey, cached.expire(),cached.expireUnit());
                    }
                }
            }
            return cacheValue;
        }
    }

    class MultipleDelegate implements Delegate {

        @Override
        @SuppressWarnings("unchecked")
        public Object apply(Detector detector, Cache<CacheKey, Object> cache, ProceedingJoinPoint point) throws MethodInvokeException {
            if (!List.class.isAssignableFrom(detector.method().getReturnType())) {
                throw new CacheException("MultipleKey注解出參必须为List.");
            }
            Cached cached = detector.cached();
            List<Object> all = new ArrayList<>();
            List<Object> cacheValues = cache.mGet(detector.cacheKeys());
            List<Object> hits = cacheValues.stream().map(o -> ((ArgumentMatcher) o).get()).collect(Collectors.toList());
            List<NoneHit> noneHits = new ArrayList<>();
            if (cacheValues.size() != detector.multipleArgument().size()) {
                for (int i = 0; i < detector.multipleArgument().size(); i++) {
                    //must override hashCode & equals method.
                    if (!hits.contains(detector.multipleArgument().get(i))) {
                        noneHits.add(new NoneHit(detector.cacheKeys().get(i), detector.multipleArgument().get(i)));
                    }
                }
            }
            List<? extends ArgumentMatcher> methodInvokeValues = new ArrayList<>();
            if (!noneHits.isEmpty()) {
                point.getArgs()[detector.multipleIndex()] = noneHits.stream()
                        .map(noneHit -> noneHit.argument).collect(Collectors.toList());
                try {
                    Object methodInvokeResult = point.proceed(point.getArgs());
                    if (methodInvokeResult != null) {
                        methodInvokeValues = (List<ArgumentMatcher>) methodInvokeResult;
                    }
                } catch (Throwable e) {
                    throw new MethodInvokeException(e);
                }
                if (!CollectionUtils.isEmpty(methodInvokeValues)) {
                    Map<Object, ArgumentMatcher> matcherMap = methodInvokeValues.stream()
                            .collect(Collectors.toMap(ArgumentMatcher::get, Function.identity(), (m1, m2) -> m2));
                    for (NoneHit noneHit:noneHits){
                        if (matcherMap.containsKey(noneHit.argument)) {
                            cache.put(noneHit.cacheKey, matcherMap.get(noneHit.argument), Condition.of(cached));
                            if (cache.keyStore()) {
                                cacheKeyRedisContainer.push(noneHit.cacheKey, cached.expire(),cached.expireUnit());
                            }
                        }
                    }
                }
            }
            all.addAll(cacheValues);
            all.addAll(methodInvokeValues);
            return all;
        }

        private class NoneHit {
            private CacheKey cacheKey;
            private Object argument;

            public NoneHit(CacheKey cacheKey, Object argument) {
                this.cacheKey = cacheKey;
                this.argument = argument;
            }
        }
    }
}
