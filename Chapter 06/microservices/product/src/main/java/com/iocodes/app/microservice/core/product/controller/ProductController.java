package com.iocodes.app.microservice.core.product.controller;

import com.iocodes.app.api.core.product.AbstractProductController;
import com.iocodes.app.api.core.product.Product;
import com.iocodes.app.microservice.core.product.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

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

    @Override
    public Product createProduct(Product body) {
        return productService.createProduct(body);
    }

    @Override
    public void deleteProduct(int productId) {
        productService.deleteProduct(productId);
    }

}
