package com.lion.demo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class ExecutionTimeAspect {

    @Around("@annotation(LogExecutionTime)") // 대문자 L
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        // Class & Method Name
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();

        String printMethod = "Method: " + className + "." + methodName + "() | ";

        // Start Time
        LocalDateTime startTime = LocalDateTime.now();
        System.out.println(printMethod + "Start time: " + startTime);

        Object result = joinPoint.proceed();    // Primary concern 실행

        // End Time
        LocalDateTime endTime = LocalDateTime.now();
        System.out.println(printMethod + "End time: " + endTime);

        // Interval Time (End - Start)
        long duration = java.time.Duration.between(startTime, endTime).toMillis();
        System.out.println(printMethod + "Duration: " + duration + " ms");

        return result;
    }
}
