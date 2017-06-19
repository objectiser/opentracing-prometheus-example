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

    @Autowired
    private io.opentracing.Tracer tracer;

    @RequestMapping("/buy")
    public String buy() {
        tracer.activeSpan().setBaggageItem("transaction", "buy");
        ResponseEntity<String> response = restTemplate.getForEntity("http://service2:8080/hello", String.class);
        return "BUY + " + response.getBody();
    }

    @RequestMapping("/sell")
    public String sell() {
        tracer.activeSpan().setBaggageItem("transaction", "sell");
        ResponseEntity<String> response = restTemplate.getForEntity("http://service2:8080/hello", String.class);
        return "SELL + " + response.getBody();
    }
}
