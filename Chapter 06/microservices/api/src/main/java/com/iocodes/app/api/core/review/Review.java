package com.iocodes.app.api.core.review;

public record Review(int reviewId, int productId, String author, double ratings, String content, String serviceAddress) {}
