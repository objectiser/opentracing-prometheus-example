package com.example.ordermgr;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderMgrController {

    @Autowired
    private RestTemplate restTemplate;

    private static String accountMgrUrl = System.getProperty("ACCOUNTMGR_URL", System.getenv("ACCOUNTMGR_URL"));

    @Autowired
    private io.opentracing.Tracer tracer;

    @RequestMapping("/buy")
    public String buy() throws InterruptedException {
        Thread.sleep(1 + (long)(Math.random()*500));
        Optional.ofNullable(tracer.activeSpan()).ifPresent(as -> as.setBaggageItem("transaction", "buy"));
        ResponseEntity<String> response = restTemplate.getForEntity(accountMgrUrl + "/account", String.class);
        return "BUY + " + response.getBody();
    }

    @RequestMapping("/sell")
    public String sell() throws InterruptedException {
        Thread.sleep(1 + (long)(Math.random()*500));
        Optional.ofNullable(tracer.activeSpan()).ifPresent(as -> as.setBaggageItem("transaction", "sell"));
        ResponseEntity<String> response = restTemplate.getForEntity(accountMgrUrl + "/account", String.class);
        return "SELL + " + response.getBody();
    }

    @RequestMapping("/fail")
    public String fail() throws InterruptedException {
        Thread.sleep(1 + (long)(Math.random()*500));
        ResponseEntity<String> response = restTemplate.getForEntity(accountMgrUrl + "/missing", String.class);
        return "FAIL + " + response.getBody();
    }
}
