package com.fintech.wallet.transfer.system.config.advice;

import com.fintech.wallet.transfer.system.application.exception.ApplicationException;
import com.fintech.wallet.transfer.system.domain.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        Map<String, Object> errorBody = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error -> errorBody.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException exception) {
        HttpStatus httpStatus = exception.getError().getHttpStatus();
        return ResponseEntity.status(httpStatus).body(bodyBuilder(exception, httpStatus));
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> handleApplicationException(ApplicationException exception) {
        HttpStatus httpStatus = exception.getError().getHttpStatus();
        return ResponseEntity.status(httpStatus).body(bodyBuilder(exception, httpStatus));
    }

    private Map<String, Object> bodyBuilder(Exception exception, HttpStatus httpStatus) {
        return Map.of(
                "code", httpStatus.value(),
                "message", exception.getMessage(),
                "timestamp", Instant.now().toString()
        );
    }
}
