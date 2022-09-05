package com.ali.multitenant.management.controllers;

import lombok.RequiredArgsConstructor;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ali.multitenant.management.domain.entity.Tenant;
import com.ali.multitenant.management.domain.entity.VerificationCode;
import com.ali.multitenant.management.service.EmailVerificationService;
import com.ali.multitenant.management.service.TenantManagementService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class TenantsApiController {

    private final TenantManagementService tenantManagementService;

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/tenants")
    public ResponseEntity<Void> createTenant(@RequestBody Tenant tenant, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        Tenant createdTenant = this.tenantManagementService.createTenant(tenant);
        VerificationCode createdVerificationCode = this.emailVerificationService.createVerificationCode(createdTenant);

        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        this.emailVerificationService.sendVerificationEmail(createdTenant.getEmail(), createdVerificationCode, scheme + "://" + serverName + ":" + serverPort);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
