package com.iocodes.app.microservice.core.review.services;


import com.iocodes.app.api.core.review.Review;
import com.iocodes.app.api.exception.InvalidInputException;
import com.iocodes.app.util.service.ServiceAddressUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    private final ServiceAddressUtil addressUtil;
    private final Logger LOG = LoggerFactory.getLogger(ReviewService.class);

    @Autowired
    public ReviewService (ServiceAddressUtil addressUtil) {
        this.addressUtil = addressUtil;
    }

    public List<Review> getReviewsByProductId(int productId) {
        if(productId < 1)
            throw new InvalidInputException("Invalid Product ID detected: " + productId);
        if(productId == 224){
            LOG.debug("No Review was Found for this product");
            return new ArrayList<>();
        }
        return List.of(
                new Review(1, productId, "Author-01", 5.0, "The product was the exact that was advertised", addressUtil.getServiceAddress()),
                new Review(2, productId, "Author-02", 4.2, "I am definitely making a purchase again in the future", addressUtil.getServiceAddress()),
                new Review(3, productId, "Author-03", 3.95, "The service I experienced there was top notch", addressUtil.getServiceAddress())
        );
    }
}
