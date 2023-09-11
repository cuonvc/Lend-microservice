package com.lend.mailservice.service.impl;

import com.lend.mailservice.payload.EmailTo;
import com.lend.mailservice.service.SendEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendEmailServiceImpl implements SendEmailService {

    private static final String SEND_FROM = "cuong.test.development@gmail.com";

    private final JavaMailSender mailSender;
    @Override
    public void send(EmailTo emailTo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(SEND_FROM);
        message.setTo(emailTo.getSendTo());
        message.setSubject(emailTo.getSubject());
        message.setText(emailTo.getContent());
        mailSender.send(message);
    }
}
