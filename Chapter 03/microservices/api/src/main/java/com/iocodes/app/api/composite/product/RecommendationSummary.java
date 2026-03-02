package com.iocodes.app.api.composite.product;

public record RecommendationSummary (
    int recommendationId,
    int productId,
    String author,
    String subject,
    String content
){}
