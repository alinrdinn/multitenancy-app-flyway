package com.ali.multitenant.management.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ali.multitenant.management.domain.entity.Tenant;
import com.ali.multitenant.management.service.EmailVerificationService;
import com.ali.multitenant.management.service.TenantManagementService;


@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;
    private final TenantManagementService tenantManagementService;

    @GetMapping(value="/verify")
    public ResponseEntity<Void> getMethodName(@RequestParam String code) {
        Tenant tenantNeedToVerified = emailVerificationService.verify(code);
        Tenant verifiedTenant = Tenant.builder()
            .email(null)
            .password(null)
            .verified(true)
            .build();
        tenantManagementService.updateTenant(verifiedTenant, tenantNeedToVerified.getTenantId());
        emailVerificationService.removeVerificationCode(code);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    
    
}
