package com.ali.multitenant.management.service;

import com.ali.multitenant.management.domain.entity.Tenant;

public interface TenantManagementService {
    
    void createTenant(Tenant tenant);
}