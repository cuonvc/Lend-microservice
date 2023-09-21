package com.lend.productservice.kafkaHandler;

import com.lend.productservice.service.CommodityService;
import com.lend.productservice.service.ProductResourceService;
import com.lend.baseservice.payload.response.FileObjectResponse;
import com.lend.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FileHandler {

    private final ProductResourceService resourceService;
    private final ProductService productService;
    private final CommodityService commodityService;

    @Bean
    public Consumer<Message<FileObjectResponse>> productImageProcess() {
        return response -> {
            String key = new String((byte[]) response.getHeaders().get(KafkaHeaders.RECEIVED_KEY));
            log.info("triggerr - key - {} - value - {}", key, response.getPayload());

            resourceService.storeImagePath(key, response.getPayload().getPath());

//            FileObjectResponse objectResponse = response.getPayload();
        };
    }

}
