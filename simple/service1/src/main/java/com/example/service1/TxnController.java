package com.example.service1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TxnController {

    @Autowired
    private RestTemplate restTemplate;

    private static String service2Url = System.getenv("SERVICE2_URL");

    @Autowired
    private io.opentracing.Tracer tracer;

    @RequestMapping("/buy")
    public String buy() throws InterruptedException {
        Thread.sleep(1 + (long)(Math.random()*500));
        tracer.activeSpan().setBaggageItem("transaction", "buy");
        ResponseEntity<String> response = restTemplate.getForEntity(service2Url + "/hello", String.class);
        return "BUY + " + response.getBody();
    }

    @RequestMapping("/sell")
    public String sell() throws InterruptedException {
        Thread.sleep(1 + (long)(Math.random()*500));
        tracer.activeSpan().setBaggageItem("transaction", "sell");
        ResponseEntity<String> response = restTemplate.getForEntity(service2Url + "/hello", String.class);
        return "SELL + " + response.getBody();
    }

    @RequestMapping("/fail")
    public String fail() throws InterruptedException {
        Thread.sleep(1 + (long)(Math.random()*500));
        ResponseEntity<String> response = restTemplate.getForEntity(service2Url + "/missing", String.class);
        return "FAIL + " + response.getBody();
    }
}
