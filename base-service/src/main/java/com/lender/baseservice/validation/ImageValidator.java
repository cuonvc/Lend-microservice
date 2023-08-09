package com.lender.baseservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class ImageValidator implements ConstraintValidator<ImageValid, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        List<String> validTypes = Arrays.asList("image/png", "image/jpg", "image/jpeg");
        boolean isSupportContentType = validTypes.contains(file.getContentType());

        if (!isSupportContentType) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Only PNG or JPG images are allowed").addConstraintViolation();
        }

        return isSupportContentType;
    }

    @Override
    public void initialize(ImageValid constraintAnnotation) {
    }
}
