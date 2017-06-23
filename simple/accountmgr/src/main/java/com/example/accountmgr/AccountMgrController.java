package com.example.accountmgr;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountMgrController {

    @RequestMapping("/account")
    public String getAccount() throws InterruptedException {
        Thread.sleep(1 + (long)(Math.random()*500));
        if (Math.random() > 0.8) {
            throw new RuntimeException("Failed to find account");
        }
        return "Account details";
    }

}
