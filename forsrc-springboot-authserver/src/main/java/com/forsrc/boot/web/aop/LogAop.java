package com.forsrc.boot.web.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Primary
public class LogAop {

    protected static final Logger LOGGER = LoggerFactory.getLogger(LogAop.class);

    @Before("execution(public * com.forsrc..*.*Controller.*(..))")
    public void before(JoinPoint jp) throws Throwable {
        LOGGER.info("[LogAop]   --> [Before] {}({})", jp.getSignature(), Arrays.toString(jp.getArgs()));
    }

    @Before("execution(public * com.forsrc..*.*Service*.*(..))")
    public void beforeService(JoinPoint jp) throws Throwable {
        LOGGER.info("[LogAop]   --> [Before] {}({})", jp.getSignature(), Arrays.toString(jp.getArgs()));
    }

    @Before("execution(public * com.forsrc..*.*Dao*.*(..))")
    public void beforeDao(JoinPoint jp) throws Throwable {
        LOGGER.info("[LogAop]   --> [Before] {}({})", jp.getSignature(), Arrays.toString(jp.getArgs()));
    }

    @After("execution(public * com.forsrc..*.*Controller.*(..))")
    public void after(JoinPoint jp) throws Throwable {
        LOGGER.info("[LogAop]   --> [After]  {}({})", jp.getSignature(), Arrays.toString(jp.getArgs()));
    }

    @Around("execution(public * com.forsrc..*.*Controller.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        long start = System.nanoTime();
        Object obj = pjp.proceed();
        long end = System.nanoTime() - start;
        LOGGER.info("[LogAop]   --> [Around] {}({}) : {}ns ", pjp.getSignature(), Arrays.toString(pjp.getArgs()),
                end);
        return obj;
    }
}
