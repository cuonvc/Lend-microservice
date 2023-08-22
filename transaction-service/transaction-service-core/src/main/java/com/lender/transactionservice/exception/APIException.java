package com.lender.transactionservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class APIException extends RuntimeException {
    private HttpStatus status;
    private String message;
}
