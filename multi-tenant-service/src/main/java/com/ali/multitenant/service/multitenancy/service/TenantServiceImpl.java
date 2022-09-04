package com.ali.multitenant.service.multitenancy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.ali.multitenant.service.multitenancy.domain.entity.Tenant;
import com.ali.multitenant.service.multitenancy.repository.TenantRepository;

@RequiredArgsConstructor
@Service
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;

    @Override
    public Tenant findByTenantId(String tenantId) {
        return tenantRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new RuntimeException("No such tenant: " + tenantId));
    }

}
