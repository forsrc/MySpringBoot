package com.forsrc.boot.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import org.springframework.context.annotation.Primary;

@Aspect
@Component
@Primary
public class LogAop {

    @Before("execution(public * com.forsrc..*.*Controller.*(..))")
    public void before(JoinPoint jp) throws Throwable {
        System.out.println(MessageFormat.format("[{0}] LogAop --> Before {1} ", new Date(), jp));
    }

    @After("execution(public * com.forsrc..*.*Controller.*(..))")
    public void after(JoinPoint jp) throws Throwable {
        System.out.println(MessageFormat.format("[{0}] LogAop --> After  {1} ", new Date(), jp));
    }

    @Around("execution(public * com.forsrc..*.*Controller.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        long start = System.nanoTime();
        Object obj = pjp.proceed();
        long end = System.nanoTime() - start;
        System.out.println(MessageFormat.format("[{0}] LogAop --> Around {1} : {2}ns ", new Date(), pjp, end));
        return obj;
    }
}
