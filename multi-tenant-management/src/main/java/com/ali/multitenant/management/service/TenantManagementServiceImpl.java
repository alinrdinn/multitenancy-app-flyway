package com.ali.multitenant.management.service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Random;

import javax.sql.DataSource;
import javax.transaction.Transactional;

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
    public Tenant createTenant(Tenant tenant) {
        Random random = new Random(System.currentTimeMillis());
        Integer randomNumber = random.nextInt(39);
        String tenantId = Hashing.sha256()
            .hashString(tenant.getEmail()+tenant.getPassword(), StandardCharsets.UTF_8)
            .toString().substring(randomNumber, randomNumber + 25);
        if ( tenantId.substring(0, 1).matches("[0-9]") ) {
            tenantId = new StringBuilder().append((random.nextInt(26) + 'a')).append(tenantId.substring(1)).toString();
        }
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
        return tenantRepository.save(tenant);
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

    @Override
    public Tenant updateTenant(Tenant updateTenant, String tenantId) {
        Optional<Tenant> tenantNeedToBeUpdateOptional = tenantRepository.findById(tenantId);
        if(!tenantNeedToBeUpdateOptional.isPresent()) {
            return null;
        }
        Tenant tenantNeedToBeUpdate = tenantNeedToBeUpdateOptional.get();
        if(updateTenant.getEmail() != null) {
            tenantNeedToBeUpdate.setEmail(updateTenant.getEmail());
        }
        if(updateTenant.getPassword() != null) {
            tenantNeedToBeUpdate.setPassword(updateTenant.getPassword());
        }
        if(updateTenant.isVerified() != tenantNeedToBeUpdate.isVerified()) {
            tenantNeedToBeUpdate.setVerified(updateTenant.isVerified());
        }
        return tenantRepository.save(tenantNeedToBeUpdate);
    }
}
