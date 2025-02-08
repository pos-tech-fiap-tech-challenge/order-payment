package com.lanchonete.order_payment.infra;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    private static final String CONTROLLERS = "execution(* com.lanchonete.order_payment.adapters.controllers..*(..))";
    private static final String SERVICES = "execution(* com.lanchonete.order_payment.core.usecase..*(..))";
    private static final String GATEWAYS = "execution(* com.lanchonete.order_payment.adapters.gateways..*(..))";
    private static final String REPOSITORIES = "execution(* com.lanchonete.order_payment.adapters.repositories..*(..))";

    @Pointcut("(" + CONTROLLERS + " || " + SERVICES + " || " + GATEWAYS + " || " + REPOSITORIES + ")")
    public void serviceMethods() {}

    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Inicia processo de log");
        logger.info("Chamando método: {} da classe: {}",
                joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getSimpleName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(value = "serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Método {} da classe {} retornou: {}",
                joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getSimpleName(),
                result);
    }

    @AfterThrowing(value = "serviceMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error("Erro no método {} da classe {}: {}",
                joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getSimpleName(),
                exception.getMessage());
    }
}
