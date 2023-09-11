//package com.lender.baseservice.exception;
//
//import com.lender.baseservice.payload.response.BaseResponse;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//@ControllerAdvice
//public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<BaseResponse<String>> handleResourceNotFound(ResourceNotFoundException exception,
//                                                                       WebRequest request) {
//        BaseResponse response = BaseResponse.<String>builder()
//                .status(HttpStatus.NOT_FOUND)
//                .dateTime(LocalDateTime.now())
//                .message(request.getDescription(false) + "\n"
//                    + exception.getMessage())
//                .build();
//
//        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(APIException.class)
//    public ResponseEntity<BaseResponse<String>> handleAPIException(APIException exception,
//                                                                       WebRequest request) {
//        BaseResponse response = BaseResponse.<String>builder()
//                .status(HttpStatus.BAD_REQUEST)
//                .dateTime(LocalDateTime.now())
//                .message(exception.getMessage())
//                .build();
//
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<BaseResponse<String>> handleGlobalException(Exception exception, WebRequest webRequest) {
//        BaseResponse response = BaseResponse.<String>builder()
//                .status(HttpStatus.BAD_REQUEST)
//                .dateTime(LocalDateTime.now())
//                .message(exception.getMessage())
//                .build();
//
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//                                                                  HttpHeaders headers,
//                                                                  HttpStatusCode status,
//                                                                  WebRequest request) {
//
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach(error -> {
//            String field = ((FieldError) error).getField();
//            String message = error.getDefaultMessage();
//            errors.put(field, message);
//        });
//
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }
//}
