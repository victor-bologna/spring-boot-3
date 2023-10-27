package com.example.springboot.controllers;

import static com.example.springboot.builder.ProductBuilder.mockProductModel;
import static com.example.springboot.builder.ProductBuilder.mockProductRecordDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.springboot.domain.dtos.ProductRecordDto;
import com.example.springboot.domain.exception.ProductNotFoundException;
import com.example.springboot.domain.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    public static final String PRODUCT_NOT_FOUND = "Product not found.";
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductController productController;

    @Test
    void saveProduct() {
        ProductRecordDto productRecordDto = mockProductRecordDto();
        ProductModel productModel = mockProductModel();

        when(productRepository.save(productModel)).thenReturn(productModel);

        ResponseEntity<ProductModel> modelResponseEntity = productController.saveProduct(productRecordDto);

        assertEquals(productModel, modelResponseEntity.getBody());
        assertEquals(HttpStatus.CREATED, modelResponseEntity.getStatusCode());

        verify(productRepository).save(productModel);
    }

    @Test
    void getAllProducts() {
        List<ProductModel> productModels = List.of(mockProductModel(), mockProductModel());

        when(productRepository.findAll()).thenReturn(productModels);

        ResponseEntity<List<ProductModel>> allProducts = productController.getAllProducts();

        assertEquals(productModels, allProducts.getBody());
        assertEquals(HttpStatus.OK, allProducts.getStatusCode());

        verify(productRepository).findAll();
    }

    @Test
    void getProduct() {
        ProductModel productModel = mockProductModel();

        when(productRepository.findById(productModel.getProductID())).thenReturn(Optional.of(productModel));

        ResponseEntity<ProductModel> product = productController.getProduct(productModel.getProductID());

        assertEquals(productModel, product.getBody());
        assertEquals(HttpStatus.OK, product.getStatusCode());

        verify(productRepository).findById(productModel.getProductID());
    }

    @Test
    void getProductNotFound() {
        ProductModel productModel = mockProductModel();

        when(productRepository.findById(productModel.getProductID())).thenReturn(Optional.empty());

        ProductNotFoundException productNotFoundException = assertThrows(ProductNotFoundException.class,
                () -> productController.getProduct(productModel.getProductID()));

        assertEquals(PRODUCT_NOT_FOUND, productNotFoundException.getMessage());

        verify(productRepository).findById(productModel.getProductID());
    }

    @Test
    void putProduct() {
        String newTestToy = "new Test Toy";
        ProductRecordDto productRecordDto = new ProductRecordDto(newTestToy, BigDecimal.valueOf(1000L));
        ProductModel productModel = mockProductModel();
        ProductModel changedProductModel = new ProductModel();

        BeanUtils.copyProperties(productModel, changedProductModel);
        changedProductModel.setName(newTestToy);

        when(productRepository.findById(productModel.getProductID())).thenReturn(Optional.of(productModel));
        when(productRepository.save(changedProductModel)).thenReturn(changedProductModel);

        ResponseEntity<ProductModel> modelResponseEntity = productController.putProduct(productModel.getProductID(), productRecordDto);

        assertEquals(changedProductModel, modelResponseEntity.getBody());
        assertEquals(HttpStatus.OK, modelResponseEntity.getStatusCode());

        verify(productRepository).findById(productModel.getProductID());
        verify(productRepository).save(changedProductModel);
    }
    @Test
    void putProductNotFound() {
        String newTestToy = "new Test Toy";
        ProductRecordDto productRecordDto = new ProductRecordDto(newTestToy, BigDecimal.valueOf(1000L));
        ProductModel productModel = mockProductModel();

        when(productRepository.findById(productModel.getProductID())).thenReturn(Optional.empty());
        ProductNotFoundException productNotFoundException = assertThrows(ProductNotFoundException.class,
                () -> productController.putProduct(productModel.getProductID(), productRecordDto));

        assertEquals(PRODUCT_NOT_FOUND, productNotFoundException.getMessage());

        verify(productRepository).findById(productModel.getProductID());
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void deleteProduct() {
        ProductModel productModel = mockProductModel();

        when(productRepository.findById(productModel.getProductID())).thenReturn(Optional.of(productModel));
        doNothing().when(productRepository).delete(productModel);

        ResponseEntity<Void> responseEntity = productController.deleteProduct(productModel.getProductID());

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

        verify(productRepository).findById(productModel.getProductID());
        verify(productRepository).delete(productModel);
    }

    @Test
    void deleteProductNotFound() {
        ProductModel productModel = mockProductModel();

        when(productRepository.findById(productModel.getProductID())).thenReturn(Optional.empty());

        ProductNotFoundException productNotFoundException = assertThrows(ProductNotFoundException.class,
                () -> productController.deleteProduct(productModel.getProductID()));

        assertEquals(PRODUCT_NOT_FOUND, productNotFoundException.getMessage());

        verify(productRepository).findById(productModel.getProductID());
        verifyNoMoreInteractions(productRepository);
    }
}