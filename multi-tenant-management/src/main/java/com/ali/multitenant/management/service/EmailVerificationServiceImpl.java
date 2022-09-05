package com.ali.multitenant.management.service;

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
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import com.ali.multitenant.management.domain.entity.Tenant;
import com.ali.multitenant.management.domain.entity.VerificationCode;
import com.ali.multitenant.management.repository.VerificationCodeRepository;

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
    public VerificationCode createVerificationCode(Tenant tenant) {
        VerificationCode verificationCode = VerificationCode.builder()
            .tenant(tenant)
            .build();
        return verificationCodeRepository.save(verificationCode);
    }

    @Override
    public void sendVerificationEmail(String tenantEmail, VerificationCode verificationCode, String siteURL) throws UnsupportedEncodingException, MessagingException {
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
        helper.setTo(tenantEmail);
        helper.setSubject(subject);

        content = content.replace("[[name]]", tenantEmail);
        String verifyURL = siteURL + "/verify?code=" + verificationCode.getVerificationCode();
        
        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
     
        mailSender.send(message);
        
    }

    @Override
    public Tenant verify(String code) {
        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findById(code);
        if (!verificationCodeOptional.isPresent()) {
            throw new RuntimeException();
        }
        VerificationCode verificationCode = verificationCodeOptional.get();
        return verificationCode.getTenant();
    }

    @Override
    public String removeVerificationCode(String code) {
        verificationCodeRepository.deleteById(code);
        return code;
    }
}
