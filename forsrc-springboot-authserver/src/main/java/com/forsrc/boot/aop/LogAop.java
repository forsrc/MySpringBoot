package com.forsrc.boot.aop;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Primary
public class LogAop {

    @Before("execution(public * com.forsrc..*.*Controller.*(..))")
    public void before(JoinPoint jp) throws Throwable {
        System.out.println(MessageFormat.format("[{0}] [LogAop]   --> [Before] {1}({2})", new Date(), jp.getSignature(),
                Arrays.toString(jp.getArgs())));
    }

    @Before("execution(public * com.forsrc..*.*Service*.*(..))")
    public void beforeService(JoinPoint jp) throws Throwable {
        System.out.println(MessageFormat.format("[{0}] [LogAop]   --> [Before] {1}({2})", new Date(), jp.getSignature(),
                Arrays.toString(jp.getArgs())));
    }

    @Before("execution(public * com.forsrc..*.*Dao*.*(..))")
    public void beforeDao(JoinPoint jp) throws Throwable {
        System.out.println(MessageFormat.format("[{0}] [LogAop]   --> [Before] {1}({2})", new Date(), jp.getSignature(),
                Arrays.toString(jp.getArgs())));
    }

    @After("execution(public * com.forsrc..*.*Controller.*(..))")
    public void after(JoinPoint jp) throws Throwable {
        System.out.println(MessageFormat.format("[{0}] [LogAop]   --> [After]  {1}({2})", new Date(), jp.getSignature(),
                Arrays.toString(jp.getArgs())));
    }

    @Around("execution(public * com.forsrc..*.*Controller.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        long start = System.nanoTime();
        Object obj = pjp.proceed();
        long end = System.nanoTime() - start;
        System.out.println(MessageFormat.format("[{0}] [LogAop]   --> [Around] {1}({2}) : {3}ns ", new Date(),
                pjp.getSignature(), Arrays.toString(pjp.getArgs()), end));
        return obj;
    }
}
