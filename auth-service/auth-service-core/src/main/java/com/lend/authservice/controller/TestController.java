package com.lend.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final StreamBridge streamBridge;

    @PostMapping
    public ResponseEntity<String> test() {
//        streamBridge.send("test-topic", "Checkkkkk");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
