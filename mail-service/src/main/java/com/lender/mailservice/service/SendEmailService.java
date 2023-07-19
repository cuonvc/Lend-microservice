package com.lender.mailservice.service;

import com.lender.mailservice.payload.EmailTo;

public interface SendEmailService {
    void send(EmailTo emailTo);
}
