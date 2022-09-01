package com.ali.multitenant.management.service;

import java.nio.charset.StandardCharsets;

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
import com.google.common.hash.Hashing;

@RequiredArgsConstructor
@Service
public class TenantManagementServiceImpl implements TenantManagementService {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    @Qualifier("tenantFlywayProperties")
    private final FlywayProperties flywayProperties;
    private final TenantRepository tenantRepository;

    private static final String VALID_TENANTID_NAME_REGEXP = "[A-Za-z0-9_]*";

    @Override
    public void createTenant(Tenant tenant) {
        Integer randomNumber = (int) Math.random() * 38;
        String tenantId = Hashing.sha256()
            .hashString(tenant.getEmail()+tenant.getPassword(), StandardCharsets.UTF_8)
            .toString().substring(randomNumber, randomNumber + 25);
        // Verify tenant ID to prevent SQL injection
        if (!tenantId.matches(VALID_TENANTID_NAME_REGEXP)) {
            throw new TenantCreationException("Invalid tenant id: " + tenantId);
        }

        try {
            createSchema(tenantId);
            runFlyway(dataSource, tenantId);
        } catch (DataAccessException e) {
            throw new TenantCreationException("Error when creating schema: " + tenantId, e);
        } catch (FlywayException e) {
            throw new TenantCreationException("Error when populating schema: ", e);
        }
        tenant.setTenantId(tenantId);
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
