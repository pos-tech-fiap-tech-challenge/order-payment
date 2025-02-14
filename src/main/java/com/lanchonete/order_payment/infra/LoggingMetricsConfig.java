package com.lanchonete.order_payment.infra;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.logging.Log4j2Metrics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingMetricsConfig {

    @Bean
    public Log4j2Metrics log4j2Metrics(MeterRegistry meterRegistry) {
        Log4j2Metrics log4j2Metrics = new Log4j2Metrics();
//        log4j2Metrics.bindTo(meterRegistry);

        return log4j2Metrics;
    }
}