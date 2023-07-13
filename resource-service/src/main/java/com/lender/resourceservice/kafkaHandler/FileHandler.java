package com.lender.resourceservice.kafkaHandler;

import com.lender.baseservice.payload.request.FileObjectRequest;
import com.lender.baseservice.payload.response.FileObjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FileHandler {

    @Bean
    public Function<FileObjectRequest, FileObjectResponse> fileHandle() {
        return request -> {
            log.info("Triggggerrrr - {}", request);
            return FileObjectResponse.builder()
                    .field("Test")
                    .url("test url")
                    .build();
        };
    }
}
