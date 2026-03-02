package com.iocodes.app.microservice.core.recommendation.controller;

import com.iocodes.app.api.core.recommendation.AbstractRecommendationController;
import com.iocodes.app.api.core.recommendation.Recommendation;
import com.iocodes.app.microservice.core.recommendation.services.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecommendationController implements AbstractRecommendationController {

    private final RecommendationService recommendationService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {
        return recommendationService.getRecommendationsByProductId(productId);
    }
}
