package org.example.newsessionproject.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AbsalyamovRuslanEmailService {
    private static final Logger log = LoggerFactory.getLogger(AbsalyamovRuslanEmailService.class);
    private final JavaMailSender jms;

    public AbsalyamovRuslanEmailService(JavaMailSender jms) {
        this.jms = jms;
    }

    @Async
    public void send(String to, String subject, String text) {
        log.info("Sending email with subject={}", subject);
        var smm = new SimpleMailMessage();
        smm.setTo(to);
        smm.setSubject(subject);
        smm.setText(text);

        try {
            jms.send(smm);
            log.info("Email sent successfully with subject={}", subject);
        } catch (Exception ex) {
            log.error("Email sending failed with subject={}", subject, ex);
            throw ex;
        }
    }
}
