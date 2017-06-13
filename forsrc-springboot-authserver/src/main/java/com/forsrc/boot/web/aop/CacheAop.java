package com.forsrc.boot.web.aop;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Scope(scopeName = "singleton")
@Primary
public class CacheAop {

    protected static final Logger LOGGER = LoggerFactory.getLogger(CacheAop.class);

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private Cache cache10s;

    private static final Map<Class<?>, Set<String>> MAP = new HashMap<>();

    @Bean(name = "cache10s")
    public Cache cache10s() {
        return cacheManager.getCache("ehcache_10s");
    }

    @Before("execution(public * com.forsrc..*.*Service.*(..))")
    public void before(JoinPoint joinPoint) throws Throwable {
        LOGGER.info("[CacheAop] --> [Before] {}({})", joinPoint.getSignature(), Arrays.toString(joinPoint.getArgs()));
    }

    @Around("execution(" + "public * com.forsrc..*.*Service.*delete*(..))"
            + " or execution(public * com.forsrc..*.*Service.*update*(..))"
            + " or execution(public * com.forsrc..*.*Service.*save*(..))"
            + " or execution(public * com.forsrc..*.*Service.cacheClear())")
    public Object aroundClear(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        LOGGER.info("[CacheAop] --> [Clear]  {}({})", proceedingJoinPoint.getSignature(),
                Arrays.toString(proceedingJoinPoint.getArgs()));
        Object obj = proceedingJoinPoint.proceed();
        // this.cache10s.clear();
        Class<?> targetClass = proceedingJoinPoint.getTarget().getClass();
        this.clear(targetClass);
        return obj;
    }

    @Around("execution(public * com.forsrc..*..service..*Service.*(..))"
            + " not (execution(public * com.forsrc..*.*Service.*update*(..))"
            + "      or execution(public * com.forsrc..*.*Service.*save*(..)))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        long start = System.nanoTime();
        Object obj = this.cache(proceedingJoinPoint);
        long end = System.nanoTime() - start;
        LOGGER.info("[CacheAop] --> [Around] {}({}) : {}ns ", proceedingJoinPoint.getSignature(),
                Arrays.toString(proceedingJoinPoint.getArgs()), end);
        return obj;
    }

    private Object cache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // String targetName =
        // proceedingJoinPoint.getTarget().getClass().getName();
        // String methodName = proceedingJoinPoint.getSignature().getName();
        Object[] arguments = proceedingJoinPoint.getArgs();

        Class<?> targetClass = proceedingJoinPoint.getTarget().getClass();
        String key = this.getCacheKey(proceedingJoinPoint);

        ValueWrapper valueWrapper = this.cache10s.get(key);

        LOGGER.info("[CacheAop] --> [Get]    {} -> {} @ {}({})", key, valueWrapper == null ? null : valueWrapper.get(),
                proceedingJoinPoint.getSignature(), Arrays.toString(proceedingJoinPoint.getArgs()));
        Object result = valueWrapper == null ? null : valueWrapper.get();

        if (result == null) {
            result = proceedingJoinPoint.proceed(arguments);
            this.cache10s.put(key, result);
            this.put(targetClass, key);
            LOGGER.info("[CacheAop] --> [Put]    {} -> {} @ {} ({})", key, result, proceedingJoinPoint.getSignature(),
                    Arrays.toString(proceedingJoinPoint.getArgs()));
        }
        return result;
    }

    private String getCacheKey(ProceedingJoinPoint proceedingJoinPoint) {
        return String.format("%s(%s)", proceedingJoinPoint.getSignature(),
                Arrays.toString(proceedingJoinPoint.getArgs()));
    }

    private String getCacheKey(String className, String methodName, Object[] arguments) {

        StringBuffer sb = new StringBuffer(200);
        sb.append(className).append('.').append(methodName).append("(");
        if (arguments != null && arguments.length > 0) {
            for (int i = 0; i < arguments.length; i++) {
                sb.append(arguments[i]).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append(")");
        return sb.toString();
    }

    private void put(Class<?> targetClass, String key) {
        Set<String> set = MAP.get(targetClass);
        if (set == null) {
            synchronized (MAP) {
                set = MAP.get(targetClass);
                if (set == null) {
                    set = new HashSet<>(20);
                    MAP.put(targetClass, set);
                }
            }
        }
        set.add(key);
    }

    private void clear(Class<?> targetClass) {
        Set<String> set = MAP.get(targetClass);
        if (set == null) {
            return;
        }
        for (String key : set) {
            this.cache10s.evict(key);
            LOGGER.info("[CacheAop] --> [Evict]  {} -> {}", key, targetClass, key);
        }
    }
}
