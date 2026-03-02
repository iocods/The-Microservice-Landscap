package com.iocodes.app.microservice.core.product.controller;

import com.iocodes.app.api.core.product.AbstractProductController;
import com.iocodes.app.api.core.product.Product;
import com.iocodes.app.api.exception.InvalidInputException;
import com.iocodes.app.api.exception.NotFoundException;
import com.iocodes.app.microservice.core.product.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_CONTENT;

@RestController
public class ProductController implements AbstractProductController {

    private final ProductService productService;


    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public Product getProduct(int productId) {
        return productService.getProduct(productId);
    }

}
