package com.lender.baseservice.exception;

import com.lender.baseservice.payload.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleResourceNotFound(ResourceNotFoundException exception,
                                                                       WebRequest request) {
        BaseResponse response = BaseResponse.<String>builder()
                .status(HttpStatus.NOT_FOUND)
                .dateTime(LocalDateTime.now())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<BaseResponse<String>> handleAPIException(ResourceNotFoundException exception,
                                                                       WebRequest request) {
        BaseResponse response = BaseResponse.<String>builder()
                .status(HttpStatus.BAD_REQUEST)
                .dateTime(LocalDateTime.now())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
