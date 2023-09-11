//package com.lender.authservice.kafkaHandler;
//
//import com.lender.authservice.payload.request.FileObjectRequest;
//import com.lender.authservice.payload.response.FileObjectResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.streams.KeyValue;
//import org.apache.kafka.streams.kstream.KStream;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.function.Consumer;
//import java.util.function.Function;
//
//@Configuration
//@Slf4j
//public class Test {
//
//    @Bean
//    public Function<FileObjectRequest, FileObjectResponse> fileHandler() {
//        return request -> {
//            log.info("Triggerr - {}", request);
//            return new FileObjectResponse().toBuilder()
//                    .field(",,,")
//                    .url("...")
//                    .build();
//        };
//    }
//
////    @Bean
////    public Consumer<KStream<String, FileObjectResponse>> testHandler() {
////        return kstream -> kstream.peek((k, v) -> log.info("testsete - {} - {}", k, v));
////    }
//}
