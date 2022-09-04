package com.ali.multitenant.service.multitenancy.config.tenant.flyway;

import java.util.Collection;
import javax.sql.DataSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import com.ali.multitenant.service.multitenancy.domain.entity.Tenant;
import com.ali.multitenant.service.multitenancy.repository.TenantRepository;

@RequiredArgsConstructor
@Getter
@Setter
public class DynamicSchemaBasedMultiTenantSpringFlyway implements InitializingBean, ResourceLoaderAware {

    private final TenantRepository masterTenantRepository;

    private final DataSource dataSource;

    @Qualifier("tenantFlywayProperties")
    private final FlywayProperties flywayProperties;

    private ResourceLoader resourceLoader;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.runOnAllSchemas(dataSource, masterTenantRepository.findAll());
    }

    protected void runOnAllSchemas(DataSource dataSource, Collection<Tenant> tenants) throws FlywayException {
        for(Tenant tenant : tenants) {
            Flyway flyway = this.getSpringFlyway(dataSource, tenant.getTenantId());
            flyway.migrate();
        }
    }

    protected Flyway getSpringFlyway(DataSource dataSource, String schema) {
        Flyway flyway = Flyway.configure()
            .schemas(schema)
            .dataSource(dataSource)
            .locations(flywayProperties.getLocations().toArray(new String[0]))
            .load();
        return flyway;
    }

}
