package com.ali.multitenant.management.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import com.ali.multitenant.management.domain.entity.Tenant;
import com.ali.multitenant.management.domain.entity.VerificationCode;

public interface EmailVerificationService {
    
    VerificationCode createVerificationCode(Tenant tenant);
    void sendVerificationEmail(String tenantEmail, VerificationCode verificationCode, String siteURL) throws UnsupportedEncodingException, MessagingException;
    Tenant verify(String code);

    String removeVerificationCode(String code);
    

}
