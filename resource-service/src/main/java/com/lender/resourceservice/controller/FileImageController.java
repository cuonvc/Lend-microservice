package com.lender.resourceservice.controller;

import com.lender.baseservice.payload.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
public class FileImageController {

    @GetMapping("/{object}/{field}")
    public ResponseEntity<BaseResponse<String>> readImageUrl(@PathVariable("object") String object,
                                                             @PathVariable("fied") String field) {
        return null;///
    }
}
