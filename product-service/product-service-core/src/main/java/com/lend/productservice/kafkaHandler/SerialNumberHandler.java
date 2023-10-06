package com.lend.productservice.kafkaHandler;

import com.lend.productservice.service.CommodityService;
import com.lend.productserviceshare.payload.response.SerialListValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Consumer;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SerialNumberHandler {

    private final CommodityService commodityService;

    @Bean
    public Consumer<Message<SerialListValue>> serialNumberDeactivate() {
        return response -> {
            String key = new String((byte[]) Optional
                    .ofNullable(response.getHeaders().get(KafkaHeaders.RECEIVED_KEY))
                    .orElse(""));
            log.info("triggerr - key - {} - value - {}", key, response.getPayload());

            commodityService.deactivateSerialNumbers(key, response.getPayload().getList());
        };
    }
}
