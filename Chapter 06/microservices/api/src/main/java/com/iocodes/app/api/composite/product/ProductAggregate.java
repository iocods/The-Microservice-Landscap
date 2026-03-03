package com.iocodes.app.api.composite.product;


import java.util.List;

public record ProductAggregate(
        int productId,
        String name,
        double weight,
        List<RecommendationSummary> recommendations,
        List<ReviewSummary> reviews,
        ServiceAddresses serviceAddresses
){}
