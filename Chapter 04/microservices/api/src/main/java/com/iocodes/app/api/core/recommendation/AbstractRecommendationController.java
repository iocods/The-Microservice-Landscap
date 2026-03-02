package com.iocodes.app.api.core.recommendation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AbstractRecommendationController {
    @GetMapping(value = "/recommendations/{productId}", produces = "application/json")
    List<Recommendation> getRecommendations(@PathVariable("productId") int productId);
}
