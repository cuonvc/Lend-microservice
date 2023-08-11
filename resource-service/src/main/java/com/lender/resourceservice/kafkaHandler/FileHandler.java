package com.lender.resourceservice.kafkaHandler;

import com.lender.baseservice.payload.request.FileObjectRequest;
import com.lender.baseservice.payload.response.FileObjectResponse;
import com.lender.resourceservice.service.FileImageService;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FileHandler {

    private final FileImageService fileImageService;

    @Bean
    public Function<Message<FileObjectRequest>, Message<FileObjectResponse>> userAvatarHandle() {
        return request -> {
            log.info("bytes - {}", request.getHeaders().get(KafkaHeaders.RECEIVED_KEY));
            String key = new String((byte[]) request.getHeaders().get(KafkaHeaders.RECEIVED_KEY));
            FileObjectRequest value = request.getPayload();
            String imagePath = fileImageService.saveAvatarFile(key, value.getField(), value.getFileBytes());

            return MessageBuilder.withPayload(FileObjectResponse.builder()
                            .path(imagePath)
                            .field(value.getField())
                    .build())
                    .setHeader(KafkaHeaders.KEY, key)
                    .build();
        };
    }

    @Bean
    public Function<Message<FileObjectRequest>, Message<FileObjectResponse>> productImageHandle() {
        return request -> {
            String key = new String((byte[]) request.getHeaders().get(KafkaHeaders.RECEIVED_KEY));
            log.info("Product trigger - key - {} - value - {}", key, request.getPayload());

            FileObjectRequest value = request.getPayload();
            String imagePath = fileImageService.saveProductImage(key, value.getField(), value.getFileBytes());

            return MessageBuilder.withPayload(FileObjectResponse.builder()
                            .path(imagePath)
                            .field(value.getField())
                            .build())
                    .setHeader(KafkaHeaders.KEY, key)
                    .build();
        };
    }
}
