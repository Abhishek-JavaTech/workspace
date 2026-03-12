package com.example.RestfulWebService.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.time.LocalDateTime;

@Aspect
@Slf4j
public class EmployeeServiceAspects {

    @Around(value = "execution(* com.example.RestfulWebService.services.EmployeeService.*(..))")
    public void employeeServiceAspect(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("starting execution of method {} at {}", proceedingJoinPoint.getSignature().getName(), LocalDateTime.now());
        log.info("with parameters: {}", proceedingJoinPoint.getArgs());
        proceedingJoinPoint.proceed();
        log.info("finishing execution of method {} at {}",proceedingJoinPoint.getSignature().getName(), LocalDateTime.now());
    }
}