package com.example.RestfulWebService.aspects;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class EmployeeRestControllerAdvice {

    @ExceptionHandler(value = RuntimeException.class)
    public Exception handleException(RuntimeException ex){
        log.error("Exception occurred: {}", ex);
        throw ex;
    }
}
