package com.lender.mailservice.kafkaHandler;

import com.lender.authserviceshare.payload.request.RegRequest;
import com.lender.mailservice.payload.EmailTo;
import com.lender.mailservice.service.SendEmailService;
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
    public Consumer<Message<String>> sendEmailProcess() {
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
}
