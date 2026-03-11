package com.iocodes.app.api.core.product;

import org.springframework.web.bind.annotation.*;

public interface AbstractProductController {
    @GetMapping(value = "/products/{productId}", produces = "application/json")
    Product getProduct(@PathVariable("productId") int productId);

    @PostMapping(value = "/products", consumes = "application/json", produces = "application/json")
    Product createProduct(@RequestBody Product body);

    @DeleteMapping(value = "/products/{productId}")
    void deleteProduct(@PathVariable("productId") int productId);
}
