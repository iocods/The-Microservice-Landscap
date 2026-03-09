package com.iocodes.app.microservice.core.recommendation.persistence;

import com.iocodes.app.microservice.core.recommendation.util.MongoDbTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest
public class RecommendationPersistenceTest extends MongoDbTestBase {

    final int PRODUCT_ID_ONE = 1;
    final int PRODUCT_ID_TWO = 2;

    @Autowired
    private RecommendationRepository recommendationRepository;

    private List<RecommendationEntity> recommendationList;

    @BeforeEach
    void setUpDb () {
        recommendationRepository.deleteAll();
        recommendationList = List.of(
            new RecommendationEntity(PRODUCT_ID_ONE, 1, "Good Product", "Author-01", "It was a very nice product I loved it"),
            new RecommendationEntity(PRODUCT_ID_ONE, 2, "Good deal", "Author-02", "A great purchase"),
            new RecommendationEntity(PRODUCT_ID_ONE, 3, "Nice Approach", "Author-03", "Amazing Customer Service")
        );

        recommendationRepository.saveAll(recommendationList);
    }

    @Test
    void testGetRecommendationByProductId() {
        List<RecommendationEntity> recommendations = recommendationRepository.findByProductId(PRODUCT_ID_ONE);
        assertEquals(3, recommendations.size());
    }

    @Test
    void testCreateRecommendation() {
        assertEquals(3, recommendationRepository.count());
        RecommendationEntity newRecommendationEntity = new RecommendationEntity(PRODUCT_ID_TWO, 4, "Good Product", "Author-01", "It was a very nice product I loved it");

        var savedRecommendationEntity = recommendationRepository.save(newRecommendationEntity);

        assertThat(savedRecommendationEntity.getContent()).isEqualTo(newRecommendationEntity.getContent());
        assertThat(savedRecommendationEntity.getSubject()).isEqualTo(newRecommendationEntity.getSubject());
        assertEquals(4, recommendationRepository.count());

    }


    @Test
    void testDeleteRecommendation() {
        assertEquals(3, recommendationRepository.count());
        List<RecommendationEntity> recommendations = recommendationRepository.findByProductId(PRODUCT_ID_ONE);
        var firstRecommendation = recommendations.getFirst();

        recommendationRepository.delete(firstRecommendation);

        assertEquals(2, recommendationRepository.count());
    }

    @Test
    void testUpdateRecommendation() {

        List<RecommendationEntity> recommendations = recommendationRepository.findByProductId(PRODUCT_ID_ONE);
        var firstRecommendation = recommendations.getFirst();
        assertEquals(0, firstRecommendation.getVersion());
        firstRecommendation.setContent("Updated content");
        firstRecommendation.setSubject("Updated subject");
        firstRecommendation.setAuthor("Updated author");
        var updatedRecommendationEntity = recommendationRepository.save(firstRecommendation);

        assertThat(updatedRecommendationEntity.getContent()).isEqualTo("Updated content");
        assertThat(updatedRecommendationEntity.getSubject()).isEqualTo("Updated subject");
        assertThat(updatedRecommendationEntity.getAuthor()).isEqualTo("Updated author");
        assertEquals(1, firstRecommendation.getVersion());
    }

    @Test
    void testDuplicateKeyError() {
        var duplicateRecommendation = new RecommendationEntity(PRODUCT_ID_ONE, 1, "Good Product", "Author-01", "It was a very nice product I loved it");
        recommendationRepository.saveAll(recommendationList);
        assertThrows(DuplicateKeyException.class, () -> saveDuplicateRecommendation(duplicateRecommendation));
    }

    void saveDuplicateRecommendation(RecommendationEntity recommendation) {
        recommendationRepository.save(recommendation);
    }
}
