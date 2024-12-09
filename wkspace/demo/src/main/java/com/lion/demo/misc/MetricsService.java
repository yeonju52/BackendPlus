package com.lion.demo.misc;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {
    @Autowired private MeterRegistry meterRegistry;

    public void recordCustomerMetrics(){
        // Counter example
        meterRegistry.counter("custom.metric.counter", "tag", "exmaple").increment();

        meterRegistry.timer("custom.metric.timer", "tag", "exmaple")
                .record(() -> {
                   try {
                       Thread.sleep(500);
                   } catch(InterruptedException e){
                       throw new RuntimeException(e);
                   }
                });

    }
}
