package com.ali.multitenant.service.services;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ali.multitenant.service.domain.entity.Employee;
import com.ali.multitenant.service.domain.entity.VerificationCode;
import com.ali.multitenant.service.multitenancy.util.TenantContext;
import com.ali.multitenant.service.repository.VerificationCodeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class EmailVerificationServiceImpl implements EmailVerificationService {
    
    @Value("${spring.mail.username}")
    private String fromAddress;

    @Autowired
    private JavaMailSender mailSender;

    private final VerificationCodeRepository verificationCodeRepository;

    @Override
    public VerificationCode createVerificationCode(Employee employee) {
        VerificationCode verificationCode = VerificationCode.builder()
            .employee(employee)
            .build();
        return verificationCodeRepository.save(verificationCode);
    }

    @Override
    public void sendVerificationEmail(String employeeEmail, VerificationCode verificationCode, String siteURL) throws UnsupportedEncodingException, MessagingException {
        String senderName = "Ali Nurdin Company";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + senderName;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
                 
        helper.setFrom(fromAddress, senderName);
        helper.setTo(employeeEmail);
        helper.setSubject(subject);

        content = content.replace("[[name]]", employeeEmail);
        String verifyURL = siteURL + "/verifyemployee?code=" + verificationCode.getVerificationCode() + "&tenantid=" + TenantContext.getTenantId();
        
        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
     
        mailSender.send(message);
        
    }

    @Override
    public Employee verify(String code) {
        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findById(code);
        if (!verificationCodeOptional.isPresent()) {
            throw new RuntimeException();
        }
        VerificationCode verificationCode = verificationCodeOptional.get();
        return verificationCode.getEmployee();
    }

    @Override
    public String removeVerificationCode(String code) {
        verificationCodeRepository.deleteById(code);
        return code;
    }
}
