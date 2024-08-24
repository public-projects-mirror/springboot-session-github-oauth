package com.example.config;

import com.example.manager.BaseAuthManager;
import com.example.manager.SessionAuthManager;
import com.example.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthManagerConfig {
    @Bean
    public BaseAuthManager sessionAuthManager( @Autowired SessionService sessionService) {
        return new SessionAuthManager(sessionService);
    }
}
