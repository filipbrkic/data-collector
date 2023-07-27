package com.data.collector.aspects;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(public * com.data.collector.controllers.MachineController.addMachine(..))")
    private void forAddingMachine() {
    }

    @Before("forAddingMachine()")
    public void beforeAddMachine() {
        System.out.println("Adding the machine to the database.");
    }

    @AfterReturning(pointcut = "forAddingMachine()", returning = "response")
    public void afterReturningAddMachine(Object response) {
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) response;
        HttpStatusCode httpStatus = responseEntity.getStatusCode();

        if (httpStatus.value() == 201) {
            System.out.println("Machine has been successfully stored to the database.");
        } else {
            System.out.println("Machine could not be successfully stored to the database.");
        }
    }
}
