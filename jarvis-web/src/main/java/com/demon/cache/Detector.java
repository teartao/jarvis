package com.demon.cache;

import com.google.common.collect.Lists;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 参数探测
 */
public class Detector {

    private boolean multiple = false;

    private boolean onlyMultiple = true;

    private String directCacheKey;

    private List<String> multipleCacheKeys = new ArrayList<>();

    private String cacheId;

    private Method method;

    private Cached cached;

    private List<Object> multipleArgument = null;

    private Integer multipleIndex = -1;

    private static final String JOIN = ":";

    public static final String SIGN = "mc:";

    private static final String PARAMETER_JOIN = "&";

    private static final String KEY_VALUE_JOIN = "=";

    public Detector(Method method, Cached cached) {
        this.cacheId = cached.id();
        this.directCacheKey = SIGN + cacheId;
        this.method = method;
        this.cached = cached;
    }

    /**
     * 探测
     *
     * @param parameter
     * @param argument
     * @param index
     */
    public void detect(Parameter parameter, Object argument, Integer index) {
        String paramName = getRealParameterName(index);
        //首先判断是不是CacheKeySegment实现类
        if (parameter.getClass().isAssignableFrom(CacheKeySegment.class)) {
            onlyMultiple = false;
            this.directCacheKey += getJoin(index) + paramName + KEY_VALUE_JOIN + ((CacheKeySegment) argument).segment();
            return;
        }
        //在者到Key注解
        Key key = parameter.getAnnotation(Key.class);
        if (key != null) {
            if(StringUtils.hasText(key.name())){
                paramName = key.name();
            }
            onlyMultiple = false;
            this.directCacheKey += getJoin(index) + paramName + KEY_VALUE_JOIN + invokeKeyMapper(key.value(), argument);
            return;
        }
        //最后为MultipleKey
        MultipleKey multipleKey = parameter.getAnnotation(MultipleKey.class);
        if (multipleKey != null) {
            if (multiple) {
                throw new CacheException("MultipleKey注解只能出现一次.");
            }
            if(!List.class.isAssignableFrom(argument.getClass())){
                throw new CacheException("MultipleKey注解参数必须为List.");
            }
            multipleArgument = new ArrayList<Object>((Collection) argument);
            if (CollectionUtils.isEmpty(multipleArgument)) {
                throw new CacheException("MultipleKey注解标注的Collection不能为空.");
            }
            if(StringUtils.hasText(multipleKey.name())){
                paramName = multipleKey.name();
            }
            multiple = true;
            multipleIndex = index;
            for (Object object : multipleArgument) {
                multipleCacheKeys.add(paramName + KEY_VALUE_JOIN + invokeKeyMapper(multipleKey.value(), object));
            }
        }

    }

    /**
     * 结果
     *
     * @return
     */
    public List<CacheKey> cacheKeys() {
        if (!multiple) {
            return Lists.newArrayList(new CacheKey(directCacheKey, cacheId));
        }
        if (multipleCacheKeys.isEmpty()) {
            throw new CacheException("MultipleKey注解标注的Collection不能为空.");
        }
        List<CacheKey> cacheKeys = new ArrayList<>();
        for (String multipleCacheKey : multipleCacheKeys) {
            cacheKeys.add(new CacheKey(directCacheKey + (onlyMultiple ? JOIN : PARAMETER_JOIN) + multipleCacheKey, cacheId));
        }
        return cacheKeys;
    }

    /**
     * 是否多维
     *
     * @return
     */
    public boolean multiple() {
        return multiple;
    }

    public Cached cached() {
        return cached;
    }

    public List<Object> multipleArgument() {
        return multipleArgument;
    }

    public Integer multipleIndex(){
        return multipleIndex;
    }

    public Method method(){
        return method;
    }

    private String getRealParameterName(Integer index) {
        try {
            ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
            if (parameterNames.length > index) {
                return parameterNames[index];
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private String getJoin(Integer index) {
        return index == 0 ? JOIN : PARAMETER_JOIN;
    }

    private String invokeKeyMapper(Class<? extends KeyMapper> clazz, Object argument) {
        try {
            return clazz.newInstance().apply(argument);
        } catch (Exception e) {
            throw new CacheException("KeyMapper " + clazz.getName() + " 运行失败");
        }
    }
}
