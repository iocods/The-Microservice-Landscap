package com.iocodes.app.microservice.core.product.services;

import com.iocodes.app.api.core.product.Product;
import com.iocodes.app.api.exception.InvalidInputException;
import com.iocodes.app.api.exception.NotFoundException;
import com.iocodes.app.util.service.ServiceAddressUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ServiceAddressUtil addressUtil;

    @Autowired
    public ProductService(ServiceAddressUtil addressUtil) {
        this.addressUtil = addressUtil;
    }

    public Product getProduct(int productId) {
        if(productId < 1)
            throw new InvalidInputException("Invalid Product ID detected: " + productId);

        if(productId == 13)
            throw new NotFoundException("No Product found for product ID: " + productId);

        return new Product(productId, "name - " + productId, 12.5, addressUtil.getServiceAddress());
    }
}
