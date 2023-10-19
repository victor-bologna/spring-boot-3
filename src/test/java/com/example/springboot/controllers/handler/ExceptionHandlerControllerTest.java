package com.example.springboot.controllers.handler;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.springboot.exception.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerControllerTest {

    @InjectMocks
    private ExceptionHandlerController exceptionHandlerController;

    @Mock
    private HttpServletRequest servletRequest;

    @Test
    void handleProductNotFoundException() {
        ProductNotFoundException productNotFoundException = new ProductNotFoundException("Product not found.");
        LocalDateTime localDateTime = LocalDateTime.now();
        ResponseError responseError = new ResponseError(localDateTime,
                HttpStatus.NOT_FOUND.getReasonPhrase(), productNotFoundException.getMessage(), "/products");

        when(servletRequest.getServletPath()).thenReturn("/products");

        ResponseEntity<ResponseError> responseErrorResponseEntity = exceptionHandlerController.handleProductNotFoundException(servletRequest,
                productNotFoundException);

        Assertions.assertTrue(responseError.timestamp().isBefore(Objects.requireNonNull(responseErrorResponseEntity.getBody()).timestamp()));
        Assertions.assertEquals(responseError.error(), responseErrorResponseEntity.getBody().error());
        Assertions.assertEquals(responseError.path(), responseErrorResponseEntity.getBody().path());
        Assertions.assertEquals(responseError.status(), responseErrorResponseEntity.getBody().status());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseErrorResponseEntity.getStatusCode());

        verify(servletRequest).getServletPath();
    }
}