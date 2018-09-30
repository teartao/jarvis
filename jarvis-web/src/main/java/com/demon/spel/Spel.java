package com.demon.spel;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;


public class Spel {

    private static final Logger LOGGER = LoggerFactory.getLogger(Spel.class);

    public static Object evaluate(ExpressionEvaluator evaluator, ProceedingJoinPoint pjp,
                           Method method, String spel, Object resultObject) {
        Object result = null;
        if (StringUtils.hasText(spel)) {
            try {
                Object target = pjp.getTarget();
                Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
                if (targetClass == null) {
                    targetClass = target.getClass();
                }
                EvaluationContext evaluationContext = evaluator.createEvaluationContext(method, pjp.getArgs(), target, targetClass,
                        resultObject != null ? resultObject : ExpressionEvaluator.NO_RESULT);
                result = evaluator.expression(spel, method, evaluationContext);
            } catch (Exception ex) {
                LOGGER.debug("parse expression error.", ex);
            }
        }
        return result;
    }

    public static Object evaluate(ExpressionEvaluator evaluator, ProceedingJoinPoint pjp, Method method, String spel) {
        return evaluate(evaluator, pjp, method, spel, null);
    }
}
