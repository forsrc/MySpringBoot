package com.forsrc.boot.aop;

import java.text.MessageFormat;
import java.util.Date;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Around;

@Aspect
@Component
public class LogAop {

    @Before("execution(public * com.forsrc..*.*Controller.*(..))")
    public void before(JoinPoint jp) throws Throwable {
        System.out.println(MessageFormat.format("[{0}] --> Before {1} ", new Date(), jp));
    }

    @After("execution(public * com.forsrc..*.*Controller.*(..))")
    public void after(JoinPoint jp) throws Throwable {
        System.out.println(MessageFormat.format("[{0}] --> After  {1} ", new Date(), jp));
    }

    @Around("execution(public * com.forsrc..*.*Controller.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        long start = System.nanoTime();
        Object obj = pjp.proceed();
        long end = System.nanoTime() - start;
        System.out.println(MessageFormat.format("[{0}] --> Around {1} : {2}ns ", new Date(), pjp, end));
        return obj;
    }
}
