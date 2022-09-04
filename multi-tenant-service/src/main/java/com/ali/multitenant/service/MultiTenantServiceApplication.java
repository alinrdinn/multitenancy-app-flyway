package com.ali.multitenant.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = { FlywayAutoConfiguration.class })
@EnableTransactionManagement
public class MultiTenantServiceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MultiTenantServiceApplication.class, args);
    }

}

