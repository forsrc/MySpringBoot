package com.forsrc.boot.aop;

import com.forsrc.constant.KeyConstants;
import com.forsrc.utils.SessionUtils;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Iterator;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EhcacheAop {

    private static final Logger LOGGER = Logger.getLogger(EhcacheAop.class);
    @Autowired
    private Ehcache cache;

    @Before("execution(public * com.forsrc..*.*Service.*(..))")
    public void before(JoinPoint jp) throws Throwable {
        System.out.println(MessageFormat.format("[{0}] EhcacheAop --> Before {1} ", new Date(), jp));
    }

    @After("execution(public * com.forsrc..*.*Service.*delete*(..)) or execution(public * com.forsrc..*.*Service.*update*(..)) or execution(public * com.forsrc..*.*Service.*save*(..))")
    public void after(JoinPoint joinPoint) throws Throwable {
        System.out.println(MessageFormat.format("[{0}] EhcacheAop --> After  {1} ", new Date(), joinPoint));
        String className = String.format("%s*", joinPoint.getTarget().getClass().getName());
        this.remove(className);
    }

    @Around("execution(public * com.forsrc..*.*Service.*(..)) not (execution(public * com.forsrc..*.*Service.*update*(..)) or execution(public * com.forsrc..*.*Service.*save*(..)))")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        long start = System.nanoTime();
        Object obj = this.ehcache(proceedingJoinPoint).getObjectValue();
        long end = System.nanoTime() - start;
        System.out.println(MessageFormat.format("[{0}] EhcacheAop --> Around {1} : {2}ns ", new Date(), proceedingJoinPoint, end));
        return obj;
    }

    private Element ehcache(ProceedingJoinPoint jp) throws Throwable {
        String targetName = jp.getTarget().getClass().getName();
        String methodName = jp.getSignature().getName();
        Object[] arguments = jp.getArgs();
        String key = this.getCacheKey(targetName, methodName, arguments);
        Element element = null;
        synchronized (this) {
            element = this.cache.get(key);
            if (element != null) {
                LOGGER.info(String.format("[Ehcache] find cache: %s", key));
                return element;
            }
            if (null == element) {
                Object result = jp.proceed(arguments);
                element = new Element(key, result);
                this.cache.put(element);
            }
        }
        return element;
    }

    private String getCacheKey(String className, String methodName, Object[] arguments) {

        StringBuffer sb = new StringBuffer(className.length() + methodName.length() + 10
                + (arguments == null ? 0 : arguments.length * 7));
        sb.append(className).append('.').append(methodName);
        if (arguments != null && arguments.length >= 0) {
            for (int i = 0; i < arguments.length; i++) {
                sb.append(".").append(arguments[i]);
            }
        }
        return sb.toString();
    }

    private void remove(final String key) {
        boolean delete = true;
        Iterator<String> it = this.cache.getKeys().iterator();
        while (it.hasNext()) {
            String cacheKey = it.next();
            if (cacheKey.indexOf(key) >= 0) {
                delete = this.cache.remove(cacheKey);
                LOGGER.info(MessageFormat.format("[Ehcache] remove cache: [{0}] {1}", delete, key));
            }
        }

    }
}
