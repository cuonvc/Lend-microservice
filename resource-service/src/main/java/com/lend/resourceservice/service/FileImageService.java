package com.lend.resourceservice.service;

import java.nio.file.Path;

public interface FileImageService {

    String saveAvatarFile(String key, String field, byte[] fileBytes);

    String saveProductImage(String key, String field, byte[] fileBytes);

    String saveCategoryImage(String key, byte[] fileBytes);

    byte[] readFileContent(Path path);
}
