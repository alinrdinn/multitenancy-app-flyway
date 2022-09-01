package com.ali.multitenant.management.service;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.stereotype.Service;

import com.ali.multitenant.management.domain.entity.Tenant;
import com.ali.multitenant.management.repository.TenantRepository;

@RequiredArgsConstructor
@Service
public class TenantManagementServiceImpl implements TenantManagementService {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    @Qualifier("tenantFlywayProperties")
    private final FlywayProperties flywayProperties;
    private final TenantRepository tenantRepository;

    private static final String VALID_SCHEMA_NAME_REGEXP = "[A-Za-z0-9_]*";

    @Override
    public void createTenant(String tenantId, String schema) {

        // Verify schema string to prevent SQL injection
        if (!schema.matches(VALID_SCHEMA_NAME_REGEXP)) {
            throw new TenantCreationException("Invalid schema name: " + schema);
        }

        try {
            createSchema(schema);
            runFlyway(dataSource, schema);
        } catch (DataAccessException e) {
            throw new TenantCreationException("Error when creating schema: " + schema, e);
        } catch (FlywayException e) {
            throw new TenantCreationException("Error when populating schema: ", e);
        }
        Tenant tenant = Tenant.builder()
                .tenantId(tenantId)
                .schema(schema)
                .build();
        tenantRepository.save(tenant);
    }

    private void createSchema(String schema) {
        jdbcTemplate.execute((StatementCallback<Boolean>) stmt -> stmt.execute("CREATE SCHEMA " + schema));
    }

    private void runFlyway(DataSource dataSource, String schema) throws FlywayException {
        Flyway flyway = getSpringFlyway(dataSource, schema);
        flyway.migrate();
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
