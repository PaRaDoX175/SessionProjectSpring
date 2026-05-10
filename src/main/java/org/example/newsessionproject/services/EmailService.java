package org.example.newsessionproject.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender jms;

    public EmailService(JavaMailSender jms) {
        this.jms = jms;
    }

    @Async
    public void send(String to, String subject, String text) {
        var smm = new SimpleMailMessage();
        smm.setTo(to);
        smm.setSubject(subject);
        smm.setText(text);

        jms.send(smm);
    }
}
