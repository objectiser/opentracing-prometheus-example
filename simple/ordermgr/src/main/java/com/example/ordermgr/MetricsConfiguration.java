package com.example.ordermgr;

import org.springframework.context.annotation.Bean;

import io.opentracing.Tracer;
//import io.opentracing.contrib.metrics.MetricLabel;
//import io.opentracing.contrib.metrics.label.BaggageMetricLabel;
//import io.opentracing.contrib.metrics.label.ConstMetricLabel;
import zipkin2.codec.SpanBytesEncoder;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.okhttp3.OkHttpSender;

import org.springframework.context.annotation.Configuration;

import brave.Tracing;
import brave.opentracing.BraveTracer;

@Configuration
public class MetricsConfiguration {

    /*
    @Bean
    public MetricLabel transactionLabel() {
        return new BaggageMetricLabel("transaction", "n/a");
    }

    @Bean
    public MetricLabel versionLabel() {
        return new ConstMetricLabel("version", System.getenv("VERSION"));
    }
    */

    @Bean
    public Tracer getTracer() {
        AsyncReporter reporter = AsyncReporter.create(OkHttpSender.create("http://zipkin.istio-system:9411/api/v2/spans"));

        // Now, create a Brave tracing component with the service name you want to see in Zipkin.
        //   (the dependency is io.zipkin.brave:brave)
        Tracing braveTracing = Tracing.newBuilder()
                              .localServiceName("ORDERMGR")
                              .spanReporter(reporter)
                              .build();

        // use this to create an OpenTracing Tracer
        return BraveTracer.create(braveTracing);
    }
}
