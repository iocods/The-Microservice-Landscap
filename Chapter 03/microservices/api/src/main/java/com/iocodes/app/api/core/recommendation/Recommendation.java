package com.iocodes.app.api.core.recommendation;

public record Recommendation(int recommendationId, int productId, double rate, String author, String content, String serviceAddress) {}
