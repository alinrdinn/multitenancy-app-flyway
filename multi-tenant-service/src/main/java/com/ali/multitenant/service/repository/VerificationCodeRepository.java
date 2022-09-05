package com.ali.multitenant.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ali.multitenant.service.domain.entity.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, String> {
    
}
