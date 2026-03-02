package com.iocodes.app.api.core.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface AbstractProductController {
    @GetMapping(value = "/products/{productId}", produces = "application/json")
    Product getProduct(@PathVariable("productId") int productId);
}
