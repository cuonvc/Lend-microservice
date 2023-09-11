package com.lend.mailservice.service;

import com.lend.mailservice.payload.EmailTo;

public interface SendEmailService {
    void send(EmailTo emailTo);
}
