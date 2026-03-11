package com.iocodes.app.api.core.review;

import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface AbstractReviewController {

    @GetMapping(value = "/reviews/{reviewId}", produces = "application/json")
    List<Review> getReviews(@PathVariable("reviewId") int reviewId);


    @PostMapping(value = "/reviews", consumes = "application/json", produces = "application/json")
    Review createReview(@RequestBody Review body);

    @DeleteMapping(value = "/reviews/{productId}")
    void deleteReviews(@PathVariable("productId") int productId);
}
