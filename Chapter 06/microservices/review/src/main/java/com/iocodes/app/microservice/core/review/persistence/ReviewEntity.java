package com.iocodes.app.microservice.core.review.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "reviews", uniqueConstraints = {@UniqueConstraint(columnNames = {"productId", "reviewId"})}, indexes = {@Index(name = "review-unique-idx", columnList = "reviewId, productId")})
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Version
    private Integer version;

    private int productId;
    private int reviewId;
    private String author;
    private String content;

    public String getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    private double rating;

    public ReviewEntity() {
    }

    public ReviewEntity(int productId, int reviewId, String author, String content, double rating) {
        this.productId = productId;
        this.reviewId = reviewId;
        this.author = author;
        this.content = content;
        this.rating = rating;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
