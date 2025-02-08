package com.lanchonete.order_payment.infra;

import com.lanchonete.order_payment.infra.logs.LoggingAspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.*;

class LoggingAspectTest {

    private LoggingAspect loggingAspect;
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspectTest.class);

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private MethodSignature methodSignature;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loggingAspect = new LoggingAspect();


    }

    @Test
    void logBefore_ShouldLogMethodInvocation() {
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getName()).thenReturn("testMethod");
        when(joinPoint.getTarget()).thenReturn(new Object());
        when(joinPoint.getArgs()).thenReturn(new Object[]{"arg1", "arg2"});

        loggingAspect.logBefore(joinPoint);

        logger.info("Chamando método: {} da classe: {}",
                joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getSimpleName());
    }
    @Test
    void logAfterReturning_ShouldLogMethodReturn() {
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getName()).thenReturn("testMethod");
        when(joinPoint.getTarget()).thenReturn(new Object());

        loggingAspect.logAfterReturning(joinPoint, "Sucesso");

        logger.info("Método {} da classe {} retornou: {}",
                joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getSimpleName(),
                "Sucesso");
    }

    @Test
    void logAfterThrowing_ShouldLogException() {
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getName()).thenReturn("testMethod");
        when(joinPoint.getTarget()).thenReturn(new Object());

        Throwable exception = new RuntimeException("Erro de teste");

        loggingAspect.logAfterThrowing(joinPoint, exception);

        logger.error("Erro no método {} da classe {}: {}",
                joinPoint.getSignature().getName(),
                joinPoint.getTarget().getClass().getSimpleName(),
                exception.getMessage());
    }
}
