package com.iocodes.app.api.composite.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface AbstractProductCompositeController {

    @GetMapping(value = "product-composite/{productId}", produces = "application/json")
    ProductAggregate getProductById (@PathVariable("productId") int productId);
}
