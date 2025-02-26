package com.example.parisjanitormsuser.service.impl;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {


    @Autowired
    private JavaMailSender mailSender;

    public void sendResetEmail(String to,String token) throws MessagingException {
        String resetLink = "http://localhost:9090/api/auth/reset-password?token="+token;

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setTo(to);
        helper.setSubject("Réinitialisation de votre mot de passe");
        helper.setText("Cliquer sur ce lien pour réinitialiser votre mot de passe : " + resetLink, true);

        mailSender.send(mimeMessage);
    }
}
