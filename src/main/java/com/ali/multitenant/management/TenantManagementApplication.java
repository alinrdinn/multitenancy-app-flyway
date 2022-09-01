package com.ali.multitenant.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
// import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(exclude = { FlywayAutoConfiguration.class })
public class TenantManagementApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(TenantManagementApplication.class, args);
    }

}

