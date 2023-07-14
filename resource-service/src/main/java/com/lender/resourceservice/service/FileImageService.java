package com.lender.resourceservice.service;

import java.nio.file.Path;

public interface FileImageService {

    String saveFile(String key, String field, byte[] fileBytes);

    byte[] readFileContent(Path path);
}
