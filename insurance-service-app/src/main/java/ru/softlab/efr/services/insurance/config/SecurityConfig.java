package ru.softlab.efr.services.insurance.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.security.Security;

@Configuration
public class SecurityConfig {

    @PostConstruct
    public void securityConfig() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }
}
