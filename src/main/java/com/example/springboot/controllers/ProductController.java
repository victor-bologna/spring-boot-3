package com.example.springboot.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.exception.ProductNotFoundException;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @PostMapping(path = "/products")
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    @GetMapping(path = "/products")
    public ResponseEntity<List<ProductModel>> getAllProducts() {
        List<ProductModel> productModelList = productRepository.findAll();
        if(!productModelList.isEmpty()) {
            productModelList.forEach(productModel -> productModel
                    .add(linkTo(methodOn(ProductController.class)
                            .getProduct(productModel.getProductID())).withSelfRel()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(productModelList);
    }

    @GetMapping(path = "/products/{id}")
    public ResponseEntity<ProductModel> getProduct(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> optionalProductModel = productRepository.findById(id);
        if (optionalProductModel.isEmpty()) {
            throw new ProductNotFoundException("Product not found.");
        }
        ProductModel productModel = optionalProductModel.get();
        productModel.add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("Products"));
        return ResponseEntity.status(HttpStatus.OK).body(productModel);
    }

    @PutMapping(path = "/products/{id}")
    public ResponseEntity<ProductModel> putProduct(@PathVariable(value = "id") UUID id,
            @RequestBody @Valid ProductRecordDto productRecordDto) {
        Optional<ProductModel> optionalProductModel = productRepository.findById(id);
        if (optionalProductModel.isEmpty()) {
            throw new ProductNotFoundException("Product not found.");
        }
        ProductModel productModel = optionalProductModel.get();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
    }

    @DeleteMapping(path = "/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> optionalProductModel = productRepository.findById(id);
        if (optionalProductModel.isEmpty()) {
            throw new ProductNotFoundException("Product not found.");
        }
        productRepository.delete(optionalProductModel.get());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
