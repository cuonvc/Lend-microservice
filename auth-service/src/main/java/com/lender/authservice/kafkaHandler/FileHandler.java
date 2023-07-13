package com.lender.authservice.kafkaHandler;

import com.lender.baseservice.payload.response.FileObjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FileHandler {

    @Bean
    public Consumer<FileObjectResponse> fileProcess() {
        return response -> log.info("Triggerr - {}", response);
    }
}
