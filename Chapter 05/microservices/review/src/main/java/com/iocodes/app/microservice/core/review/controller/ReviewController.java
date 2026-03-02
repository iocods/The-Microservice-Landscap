package com.iocodes.app.microservice.core.review.controller;

import com.iocodes.app.api.core.review.AbstractReviewController;
import com.iocodes.app.api.core.review.Review;
import com.iocodes.app.microservice.core.review.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReviewController implements AbstractReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Override
    public List<Review> getReviews(int productId) {
        return reviewService.getReviewsByProductId(productId);
    }
}
