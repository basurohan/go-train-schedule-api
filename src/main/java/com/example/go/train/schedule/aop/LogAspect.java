package com.example.go.train.schedule.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Order(0)
@Aspect
@Configuration
public class LogAspect {
    @Around(value = "com.example.go.train.schedule.aop.AppPointcuts.mainPointcut()")
    public Object calculateMethodTime(ProceedingJoinPoint joinPoint) throws Throwable {
        final Logger classLogger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());

        String className = joinPoint.getTarget().getClass().getName();
        String methodName = ((MethodSignature)joinPoint.getSignature()).getMethod().getName();
        String methodArgs = Stream.of(joinPoint.getArgs()).collect(Collectors.toList()).toString();
        Long startTime = System.nanoTime();
        Object result = joinPoint.proceed();
        Long endTime = System.nanoTime();
        Long elapsedTime = endTime-startTime;

        LogMessage message = LogMessage.builder()
                .className(className)
                .methodName(methodName)
                .methodArgs(methodArgs)
                .elapsedTimeInMills(TimeUnit.NANOSECONDS.toMillis(elapsedTime))
                .elapsedTimeInMicros(TimeUnit.NANOSECONDS.toMicros(elapsedTime))
                .result(result)
                .build();

        classLogger.info("LogAspect: " + message);

        return result;
    }
}
