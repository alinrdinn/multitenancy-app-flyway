package com.ali.multitenant.management.config;

import javax.sql.DataSource;
// import liquibase.integration.spring.SpringLiquibase;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
// import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Lazy(false)
@Configuration
@ConditionalOnProperty(name = "multitenancy.master.flyway.enabled", havingValue = "true", matchIfMissing = true)
public class FlywayConfig {

    @Value("${multitenancy.master.schema:public}")
    private String masterSchema;

    @Bean
    @ConfigurationProperties("multitenancy.master.flyway")
    public FlywayProperties masterFlywayProperties() {
        return new FlywayProperties();
    }

    @Bean
    @ConfigurationProperties("multitenancy.tenant.flyway")
    public FlywayProperties tenantFlywayProperties() {
        return new FlywayProperties();
    }

    @Bean
    public Flyway flyway(ObjectProvider<DataSource> flywayDataSource) {
        FlywayProperties flywayProperties = masterFlywayProperties();
        Flyway flyway = Flyway.configure()
            .defaultSchema(this.masterSchema)
            .dataSource(flywayDataSource.getIfAvailable())
            .locations(flywayProperties.getLocations().toArray(new String[0]))
            .load();
        flyway.migrate();
        return flyway;
    }

}
