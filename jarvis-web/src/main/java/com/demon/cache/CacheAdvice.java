package com.demon.cache;

import com.alibaba.fastjson.JSON;
import com.demon.spel.ExpressionEvaluator;
import com.demon.spel.Spel;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Component
@Aspect
public class CacheAdvice {

    @Resource
    private AopCacheOperationDelegate delegate;

    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheAdvice.class);

    @Around("@annotation(cached)")
    public Object cache(ProceedingJoinPoint point, Cached cached) throws Throwable {
        try {
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            if (method.getReturnType().equals(Void.TYPE)) {
                throw new CacheException("方法没有返回值");
            }
            Parameter[] parameters = method.getParameters();
            Object[] arguments = point.getArgs();

            Detector detector = new Detector(method, cached);

            for (int i = 0; i < parameters.length; i++) {
                detector.detect(parameters[i], arguments[i], i);
            }
            Object result = delegate.cached(detector, point);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("result:" + JSON.toJSONString(result));
            }
            return result;
        } catch (MethodInvokeException e) {
            throw e.getCause();
        } catch (Throwable e) {
            LOGGER.error("cached error", e);
            return point.proceed();
        }
    }

    @Around("@annotation(cachedRemove)")
    public void remove(ProceedingJoinPoint point, CachedRemove cachedRemove) throws Throwable {
        point.proceed();
        try {
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            String id = StringUtils.hasText(cachedRemove.spelId())
                    ? Spel.evaluate(evaluator, point, method, cachedRemove.spelId()).toString()
                    : cachedRemove.id();
            Media media = StringUtils.hasText(cachedRemove.spelMedia())
                    ? Media.of(Spel.evaluate(evaluator, point, method, cachedRemove.spelMedia()).toString())
                    : cachedRemove.media();
            delegate.remove(id, media);
        } catch (Exception e) {
            LOGGER.error("cache remove error", e);
        }
    }
}
