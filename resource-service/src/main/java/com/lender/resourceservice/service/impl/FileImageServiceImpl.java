package com.lender.resourceservice.service.impl;

import com.lender.resourceservice.service.FileImageService;
import com.lender.resourceservice.utils.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class FileImageServiceImpl implements FileImageService {

    private final FileUploadUtils fileUploadUtils;

    @Override
    public String saveFile(String key, String field, byte[] fileBytes) {
        String uploadDir = "image/user/avatar/" + key;
        Path path = fileUploadUtils.saveFile(uploadDir, fileBytes);
        return path.toString().replace("\\", "/");
    }
}
