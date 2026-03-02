package com.iocodes.app.microservice.composite.product.services;

import com.iocodes.app.api.composite.product.ProductAggregate;
import com.iocodes.app.api.composite.product.RecommendationSummary;
import com.iocodes.app.api.composite.product.ReviewSummary;
import com.iocodes.app.api.composite.product.ServiceAddresses;
import com.iocodes.app.api.core.product.AbstractProductController;
import com.iocodes.app.api.core.product.Product;
import com.iocodes.app.api.core.recommendation.AbstractRecommendationController;
import com.iocodes.app.api.core.recommendation.Recommendation;
import com.iocodes.app.api.core.review.AbstractReviewController;
import com.iocodes.app.api.core.review.Review;
import com.iocodes.app.util.service.ServiceAddressUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class ProductAggregateService implements AbstractProductController, AbstractRecommendationController, AbstractReviewController {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final ServiceAddressUtil addressUtil;


    private final String productServiceUrl;
    private final String recommendationServiceUrl;
    private final String reviewServiceUrl;


    @Autowired
    public ProductAggregateService(
            RestTemplate restTemplate,
            ObjectMapper mapper,
            ServiceAddressUtil addressUtil,
            @Value("${app.product-service.host}") String productServiceHost,
            @Value("${app.product-service.port}")String productServicePort,
            @Value("${app.recommendation-service.host}") String recommendationServiceHost,
            @Value("${app.recommendation-service.port}") String recommendationServicePort,
            @Value("${app.review-service.host}") String reviewServiceHost,
            @Value("${app.review-service.port}") String reviewServicePort
    ) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.addressUtil = addressUtil;
        this.productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/products";
        this.recommendationServiceUrl =  "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/recommendations";;
        this.reviewServiceUrl =  "http://" + reviewServiceHost + ":" + reviewServicePort + "/reviews";;
    }

    @Override
    public Product getProduct(int productId) {
        String url = productServiceUrl + "/" + productId;
        return restTemplate.getForObject(url, Product.class);
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {
        String url = recommendationServiceUrl + "/" + productId;
        return restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Recommendation>>() {}).getBody();
    }

    @Override
    public List<Review> getReviews(int productId) {
        String url = reviewServiceUrl + "/" + productId;
        return restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Review>>() {}).getBody();
    }

    public ProductAggregate getProductAggregateById(int productId) {
        Product product = getProduct(productId);
        List<Review> reviews = getReviews(productId);
        List<Recommendation> recommendations = getRecommendations(productId);

        return createAggregateProduct(product, reviews, recommendations);
    }

    private ProductAggregate createAggregateProduct(Product product, List<Review> reviews, List<Recommendation> recommendations) {
        // map reviews to review-summaries
        List<ReviewSummary> reviewSummaries = reviews
                .stream()
                .map(
                        review -> mapper.convertValue(review, ReviewSummary.class)
                ).toList();

        // map recommendations to recommendation-summaries
        List<RecommendationSummary> recommendationSummaries = recommendations
                .stream()
                .map(
                        recommendation -> mapper.convertValue(recommendation, RecommendationSummary.class)
                ).toList();

        // create service addresses.
        ServiceAddresses serviceAddresses = new ServiceAddresses(addressUtil.getServiceAddress(), productServiceUrl, recommendationServiceUrl, reviewServiceUrl);

        return new ProductAggregate(product.productId(), product.name(), product.weight(), recommendationSummaries, reviewSummaries, serviceAddresses);

    }
}
