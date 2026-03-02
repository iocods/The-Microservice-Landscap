package com.iocodes.app.microservice.composite.product.controller;

import com.iocodes.app.api.composite.product.AbstractProductCompositeController;
import com.iocodes.app.api.composite.product.ProductAggregate;
import com.iocodes.app.api.exception.InvalidInputException;
import com.iocodes.app.api.exception.NotFoundException;
import com.iocodes.app.microservice.composite.product.services.ProductAggregateService;
import com.iocodes.app.util.error.HttpErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import tools.jackson.databind.ObjectMapper;

@RestController
public class ProductCompositeController implements AbstractProductCompositeController {

    private final ProductAggregateService aggregateService;
    private final ObjectMapper mapper;

    private final Logger LOG = LoggerFactory.getLogger(ProductCompositeController.class);

    @Autowired
    public ProductCompositeController(ProductAggregateService aggregateService, ObjectMapper mapper) {
        this.aggregateService = aggregateService;
        this.mapper = mapper;
    }

    @Override
    public ProductAggregate getProductById(int productId) {
        try{
            return aggregateService.getProductAggregateById(productId);
        }
         catch (HttpClientErrorException ex) {
            HttpStatus code = HttpStatus.resolve(ex.getStatusCode().value());
            LOG.debug("Resolving Error Code : " + code.name());
            switch (code) {
                case NOT_FOUND -> throw new NotFoundException(getErrorMessage(ex));
                case UNPROCESSABLE_CONTENT -> throw new InvalidInputException(getErrorMessage(ex));
                case null, default -> throw new RuntimeException("An Error Occurred while processing the request");
            }
        }
    }


    private String getErrorMessage(HttpClientErrorException ex) {
        var message = ex.getResponseBodyAsString();

        HttpErrorResponse errorResponse = mapper.readValue(message, HttpErrorResponse.class);

        return errorResponse.message();
    }
}
