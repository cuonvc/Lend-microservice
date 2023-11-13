package com.lend.resourceservice.service.impl;

import com.lend.resourceservice.utils.FileUploadUtils;
import com.lend.resourceservice.service.FileImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class FileImageServiceImpl implements FileImageService {

    private final FileUploadUtils fileUploadUtils;
    private final RestTemplate restTemplate;

    @Override
    public String saveAvatarFile(String key, String field, byte[] fileBytes) {
        String uploadDir = "image/user/avatar/" + key;
        Path path = fileUploadUtils.saveFile(uploadDir, fileBytes);
        return path.toString().replace("\\", "/");
    }

    @Override
    public String saveProductImage(String key, String field, byte[] fileBytes) {
        String uploadDir = "image/product/" + key;
        Path path = fileUploadUtils.saveFile(uploadDir, fileBytes);
        return path.toString().replace("\\", "/");
    }

    @Override
    public String saveCategoryImage(String key, byte[] fileBytes) {
        String uploadDir = "image/category/" + key;
        Path path = fileUploadUtils.saveFile(uploadDir, fileBytes);
        return path.toString().replace("\\", "/");
    }

    @Override
    public byte[] readFileContent(Path path) {
        try {
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return StreamUtils.copyToByteArray(resource.getInputStream());
            } else {
                throw new RuntimeException("Could not read file");
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read file");
        }
    }
}
