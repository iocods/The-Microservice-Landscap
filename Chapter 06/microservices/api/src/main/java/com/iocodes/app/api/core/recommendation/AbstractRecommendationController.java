package com.iocodes.app.api.core.recommendation;

import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface AbstractRecommendationController {
    @GetMapping(value = "/recommendations/{productId}", produces = "application/json")
    List<Recommendation> getRecommendations(@PathVariable("productId") int productId);


    @PostMapping(value = "/recommendations", consumes = "application/json", produces = "application/json")
    Recommendation createRecommendation(@RequestBody Recommendation body);

    @DeleteMapping(value = "/recommendations/{productId}")
    void deleteRecommendation(@PathVariable("productId") int productId);
}
