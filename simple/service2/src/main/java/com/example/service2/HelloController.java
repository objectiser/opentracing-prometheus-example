package com.example.service2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private io.opentracing.Tracer tracer;

    @RequestMapping("/hello")
    public String hello() throws InterruptedException {
        Thread.sleep(1 + (long)(Math.random()*500));
        if (Math.random() > 0.8) {
            tracer.activeSpan().setTag("error", true);
        }
        return "Hello from Spring Boot!";
    }

}
