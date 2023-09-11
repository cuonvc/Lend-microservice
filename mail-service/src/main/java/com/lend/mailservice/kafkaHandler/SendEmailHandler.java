package com.lend.mailservice.kafkaHandler;

import com.lend.mailservice.payload.EmailTo;
import com.lend.mailservice.service.SendEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SendEmailHandler {

    private final SendEmailService sendEmailService;

    @Bean
    public Consumer<Message<String>> sendEmailActiveAccount() {
        return request -> {
            String emailTo = new String((byte[]) request.getHeaders().get(KafkaHeaders.RECEIVED_KEY));
            log.info("key - {} - value - {}", new String((byte[]) request.getHeaders().get(KafkaHeaders.RECEIVED_KEY)), request.getPayload());

            sendEmailService.send(EmailTo.builder()
                            .sendTo(emailTo)
                            .subject("Active Lender account")
                            .content("Your activation code: " + request.getPayload())
                    .build());
        };
    }

    @Bean
    public Consumer<Message<String>> sendEmailForgotPassword() {
        return request -> {
            String emailTo = new String((byte[]) request.getHeaders().get(KafkaHeaders.RECEIVED_KEY));
            log.info(emailTo);
            log.info("key - {} - value - {}", emailTo, request.getPayload());

            sendEmailService.send(EmailTo.builder()
                            .sendTo(emailTo)
                            .subject("Confirm renew password")
                            .content("Your confirm code to forgot password: " + request.getPayload())
                    .build());
        };
    }
}
