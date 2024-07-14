package com.n1nt3nd0.notificationservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender javaMailSender;
    public void sendMessage(String toEmail, String subject, String text){
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(text);
            log.info("Mail: %s, subject: %s, text: %s".formatted(toEmail, subject, text));
        } catch (MessagingException e) {
            log.error("Error while sending mail", e);
        }
        javaMailSender.send(mimeMessage);
    }
}