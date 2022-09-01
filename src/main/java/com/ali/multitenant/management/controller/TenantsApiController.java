package com.ali.multitenant.management.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ali.multitenant.management.service.TenantManagementService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class TenantsApiController {

    private final TenantManagementService tenantManagementService;

    @PostMapping("/tenants")
    public ResponseEntity<Void> createTenant(@RequestParam String tenantId, @RequestParam String schema) {
        this.tenantManagementService.createTenant(tenantId, schema);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
