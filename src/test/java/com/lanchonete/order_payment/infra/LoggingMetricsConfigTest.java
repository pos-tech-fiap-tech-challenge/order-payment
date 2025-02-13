package com.lanchonete.order_payment.infra;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.logging.Log4j2Metrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoggingMetricsConfigTest {

    private LoggingMetricsConfig loggingMetricsConfig;
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = mock(MeterRegistry.class);
        loggingMetricsConfig = new LoggingMetricsConfig();
    }

    @Test
    void log4j2Metrics_ShouldBindToMeterRegistry() {
        Log4j2Metrics log4j2Metrics = loggingMetricsConfig.log4j2Metrics(meterRegistry);

        assertNotNull(log4j2Metrics, "O Log4j2Metrics não deveria ser nulo");

        verify(meterRegistry, times(1));
    }
}
