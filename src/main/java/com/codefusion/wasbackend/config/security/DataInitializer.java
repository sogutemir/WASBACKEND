package com.codefusion.wasbackend.config.security;

import com.codefusion.wasbackend.Account.service.AccountService;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final AccountService accountService;

    public DataInitializer(AccountService accountService) {
        this.accountService = accountService;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        accountService.initializeUsers();
    }
}
