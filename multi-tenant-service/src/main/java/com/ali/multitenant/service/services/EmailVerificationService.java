package com.ali.multitenant.service.services;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import com.ali.multitenant.service.domain.entity.Employee;
import com.ali.multitenant.service.domain.entity.VerificationCode;

public interface EmailVerificationService {
    
    VerificationCode createVerificationCode(Employee employee);
    void sendVerificationEmail(String employeeEmail, VerificationCode verificationCode, String siteURL) throws UnsupportedEncodingException, MessagingException;
    Employee verify(String code);
    String removeVerificationCode(String code);
    

}
