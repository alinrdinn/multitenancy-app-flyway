package com.ali.multitenant.management.service;

import com.ali.multitenant.management.domain.entity.Tenant;

public interface TenantManagementService {
    
    Tenant createTenant(Tenant tenant);

    Tenant updateTenant(Tenant updateTenant, String tenantId);
}