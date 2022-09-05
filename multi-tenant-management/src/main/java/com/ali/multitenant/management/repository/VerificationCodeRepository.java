package com.ali.multitenant.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ali.multitenant.management.domain.entity.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, String> {
    
}
