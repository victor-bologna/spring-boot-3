package com.example.springboot.builder;

import com.example.springboot.domain.dtos.ProductRecordDto;
import com.example.springboot.domain.models.ProductModel;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.beans.BeanUtils;

public class ProductBuilder {
    public static ProductRecordDto mockProductRecordDto() {
        return new ProductRecordDto("Test Toy", new BigDecimal("1500.00"));
    }
    public static ProductModel mockProductModel() {
        ProductModel productModel = new ProductModel();
        productModel.setProductID(UUID.randomUUID());
        BeanUtils.copyProperties(mockProductRecordDto(), productModel);
        return productModel;
    }
}
