package com.ali.multitenant.service.multitenancy.service;

import org.springframework.data.repository.query.Param;

import com.ali.multitenant.service.multitenancy.domain.entity.Tenant;

public interface TenantService {
    
    Tenant findByTenantId(@Param("tenantId") String tenantId);
}
