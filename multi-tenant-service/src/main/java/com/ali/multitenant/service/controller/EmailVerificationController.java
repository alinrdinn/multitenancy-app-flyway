package com.ali.multitenant.service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ali.multitenant.service.domain.entity.Employee;
import com.ali.multitenant.service.multitenancy.util.TenantContext;
import com.ali.multitenant.service.services.EmailVerificationService;
import com.ali.multitenant.service.services.EmployeeService;


@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;
    private final EmployeeService employeeService;

    @GetMapping(value="/verifyemployee")
    public ResponseEntity<Void> getMethodName(@RequestParam String code, @RequestParam String tenantid) {
        TenantContext.setTenantId(tenantid);
        Employee employeeNeedToVerified = emailVerificationService.verify(code);
        Employee verifiedEmployee = Employee.builder()
            .email(null)
            .password(null)
            .verified(true)
            .build();
        employeeService.updateEmployee(verifiedEmployee, employeeNeedToVerified.getEmail());
        emailVerificationService.removeVerificationCode(code);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    
    
}
