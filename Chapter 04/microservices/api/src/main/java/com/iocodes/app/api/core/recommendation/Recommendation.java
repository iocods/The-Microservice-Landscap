package com.iocodes.app.api.core.recommendation;

public record Recommendation(int recommendationId, int productId, String subject, String author, String content, String serviceAddress) {}
