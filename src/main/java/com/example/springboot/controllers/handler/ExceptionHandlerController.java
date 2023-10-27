package com.example.springboot.controllers.handler;

import com.example.springboot.domain.exception.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = ProductNotFoundException.class)
    ResponseEntity<ResponseError> handleProductNotFoundException(HttpServletRequest request,
            final ProductNotFoundException productNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseError(LocalDateTime.now(), HttpStatus.NOT_FOUND.getReasonPhrase(),
                        productNotFoundException.getMessage(), request.getServletPath()));
    }
}
