package com.lender.resourceservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

@Slf4j
@Component
public class FileUploadUtils {

    public Path saveFile(String uploadDir, byte[] fileBytes) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID().toString().replace("-", "");
            fileName = fileName + ".png";

            File directory = uploadPath.toFile();
            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile()) {
                            file.delete();
                        }
                    }
                }
            }

            return Files.write(uploadPath.resolve(fileName), fileBytes, StandardOpenOption.CREATE);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }
}
