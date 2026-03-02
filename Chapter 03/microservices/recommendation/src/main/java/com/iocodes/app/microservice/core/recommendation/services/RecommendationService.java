package com.iocodes.app.microservice.core.recommendation.services;


import com.iocodes.app.api.core.recommendation.Recommendation;
import com.iocodes.app.api.exception.InvalidInputException;
import com.iocodes.app.util.service.ServiceAddressUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationService {
    private final Logger LOG = LoggerFactory.getLogger(RecommendationService.class);

    private final ServiceAddressUtil addressUtil;

    @Autowired
    public RecommendationService(ServiceAddressUtil addressUtil) {
        this.addressUtil = addressUtil;
    }

    public List<Recommendation> getRecommendationsByProductId (int productId) {
        if(productId < 1)
            throw new InvalidInputException("Invalid Product ID detected: " + productId);
        if(productId == 114){
            LOG.debug("No Recommendation Found for this product");
            return new ArrayList<>();
        }
        return List.of(
                new Recommendation(1, productId, 1.5, "Author-01", "Nice Product", addressUtil.getServiceAddress()),
                new Recommendation(2, productId, 2.5, "Author-02", "A great purchase", addressUtil.getServiceAddress()),
                new Recommendation(3, productId, 3.5, "Author-03", "Amazing Customer Service", addressUtil.getServiceAddress())
        );
    }
}
