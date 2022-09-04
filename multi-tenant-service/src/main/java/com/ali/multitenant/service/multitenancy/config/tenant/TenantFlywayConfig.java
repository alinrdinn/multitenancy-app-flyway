package com.ali.multitenant.service.multitenancy.config.tenant;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.ali.multitenant.service.multitenancy.config.tenant.flyway.DynamicSchemaBasedMultiTenantSpringFlyway;
import com.ali.multitenant.service.multitenancy.repository.TenantRepository;

@Lazy(false)
@Configuration
@ConditionalOnProperty(name = "multitenancy.tenant.flyway.enabled", havingValue = "true", matchIfMissing = true)
public class TenantFlywayConfig {

    @Bean
    @ConfigurationProperties("multitenancy.tenant.flyway")
    public FlywayProperties tenantFlywayProperties() {
        return new FlywayProperties();
    }

    @Bean
    public DynamicSchemaBasedMultiTenantSpringFlyway tenantFlyway(
        TenantRepository masterTenantRepository,
        DataSource dataSource,
        @Qualifier("tenantFlywayProperties")
        FlywayProperties flywayProperties) {
        return new DynamicSchemaBasedMultiTenantSpringFlyway(masterTenantRepository, dataSource, flywayProperties);
    }

}
