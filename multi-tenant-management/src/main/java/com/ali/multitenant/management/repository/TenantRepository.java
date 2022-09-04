package com.ali.multitenant.management.repository;

import org.springframework.data.repository.CrudRepository;

import com.ali.multitenant.management.domain.entity.Tenant;

public interface TenantRepository extends CrudRepository<Tenant, String> {
}