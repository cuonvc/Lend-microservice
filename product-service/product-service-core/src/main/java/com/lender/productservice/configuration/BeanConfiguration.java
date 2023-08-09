package com.lender.productservice.configuration;

import com.lender.baseservice.payload.response.ResponseFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfiguration {

    @Bean
    public ResponseFactory responseFactory() {
        return new ResponseFactory();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
