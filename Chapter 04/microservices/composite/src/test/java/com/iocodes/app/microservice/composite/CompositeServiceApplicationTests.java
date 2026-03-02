package com.iocodes.app.microservice.composite;

import com.iocodes.app.api.composite.product.ProductAggregate;
import com.iocodes.app.api.composite.product.RecommendationSummary;
import com.iocodes.app.api.composite.product.ReviewSummary;
import com.iocodes.app.api.composite.product.ServiceAddresses;
import com.iocodes.app.api.exception.InvalidInputException;
import com.iocodes.app.api.exception.NotFoundException;
import com.iocodes.app.microservice.composite.product.services.ProductAggregateService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = RANDOM_PORT)
class CompositeServiceApplicationTests {


	private static final int PRODUCT_ID_OK = 1;
	private static final int PRODUCT_ID_INVALID = -12;
	private static final int PRODUCT_ID_NOT_FOUND = 13;

	private static ProductAggregate productAggregate;

	@BeforeAll
	static void setUp() {
		List<RecommendationSummary> recommendations = List.of(
				new RecommendationSummary(1, PRODUCT_ID_OK, "Recommendation Author", "Author-01", "Nice Product")
		);
		List<ReviewSummary> reviews = List.of(
				new ReviewSummary(1, PRODUCT_ID_OK, "Author-01", 5.0, "The product was the exact that was advertised")
		);
		ServiceAddresses serviceAddresses = new ServiceAddresses(
				"http://localhost:7000",
				"http://localhost:7001",
				"http://localhost:7002",
				"http://localhost:7003"
		);
		productAggregate = new ProductAggregate(PRODUCT_ID_OK, "name-1", 12.5, recommendations, reviews, serviceAddresses);
	}

	@Autowired
	private WebTestClient testClient;

	@MockitoBean
	private ProductAggregateService aggregateService;

	@Test
	void contextLoads() {
		assertNotNull(testClient);
		assertNotNull(aggregateService);
	}

	@BeforeEach
	void init() {
		when(aggregateService.getProductAggregateById(PRODUCT_ID_OK)).thenReturn(productAggregate);

		when(aggregateService.getProductAggregateById(PRODUCT_ID_INVALID)).thenThrow(new InvalidInputException("Invalid Product ID detected: " + PRODUCT_ID_INVALID));
		when(aggregateService.getProductAggregateById(PRODUCT_ID_NOT_FOUND)).thenThrow(new NotFoundException("No Product found for product ID: " + PRODUCT_ID_NOT_FOUND));
	}


	@Test
	void getProductById() {
		testClient.get().uri("/product-composite/" + PRODUCT_ID_OK)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBody().jsonPath("$.productId").isEqualTo(PRODUCT_ID_OK)
				.jsonPath("$.recommendations.length()").isEqualTo(1)
				.jsonPath("$.reviews.length()").isEqualTo(1)
				.jsonPath("$.name").isEqualTo("name-1");
	}


	@Test
	void getNotFoundProduct() {
		testClient.get().uri("/product-composite/" + PRODUCT_ID_NOT_FOUND)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isEqualTo(404)
				.expectBody().jsonPath("$.message").isEqualTo("No Product found for product ID: " + PRODUCT_ID_NOT_FOUND)
				.jsonPath("$.path").isEqualTo("/product-composite/" + PRODUCT_ID_NOT_FOUND);
	}
}
