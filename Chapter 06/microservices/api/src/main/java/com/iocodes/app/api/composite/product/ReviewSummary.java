package com.iocodes.app.api.composite.product;

public record ReviewSummary (
        int reviewId,
        int productId,
        String author,
        double ratings,
        String content
){}
