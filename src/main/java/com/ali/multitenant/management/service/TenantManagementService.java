package com.ali.multitenant.management.service;

public interface TenantManagementService {
    
    void createTenant(String tenantId, String schema);
}