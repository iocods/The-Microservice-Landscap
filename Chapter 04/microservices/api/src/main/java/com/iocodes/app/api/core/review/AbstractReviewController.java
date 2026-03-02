package com.iocodes.app.api.core.review;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AbstractReviewController {

    @GetMapping(value = "/reviews/{reviewId}", produces = "application/json")
    List<Review> getReviews(@PathVariable("reviewId") int reviewId);
}
