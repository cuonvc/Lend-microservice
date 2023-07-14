package com.lender.resourceservice.controller;

import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.resourceservice.service.FileImageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
public class FileImageController {

    private final AntPathMatcher antPathMatcher;
    private final FileImageService fileImageService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        String currentDomain = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/**")
    public ResponseEntity<byte[]> readImageUrl(HttpServletRequest request) {
        //Pending business
            //save file to third-party -> calling from this
            //or save file to resource-service
        String uri = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String path = antPathMatcher.extractPathWithinPattern(pattern, uri);

        Path toPath = Paths.get(path);
        try {
            byte[] bytes = fileImageService.readFileContent(toPath);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(bytes);
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }
}
