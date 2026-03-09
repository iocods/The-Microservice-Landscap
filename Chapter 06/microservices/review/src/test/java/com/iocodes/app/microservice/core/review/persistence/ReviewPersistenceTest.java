package com.iocodes.app.microservice.core.review.persistence;

import com.iocodes.app.microservice.core.review.util.PostgreSqlTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReviewPersistenceTest extends PostgreSqlTestBase {


    final int PRODUCT_ID_ONE = 1;
    final int PRODUCT_ID_TWO = 2;

    @Autowired
    private ReviewRepository recommendationRepository;

    private List<ReviewEntity> recommendationList;

    @BeforeEach
    void setUpDb () {
        recommendationRepository.deleteAll();
        recommendationList = List.of(
                new ReviewEntity(PRODUCT_ID_ONE, 1, "Good Product", "Author-01", 4.3),
                new ReviewEntity(PRODUCT_ID_ONE, 2, "Good deal", "Author-02", 4.55),
                new ReviewEntity(PRODUCT_ID_ONE, 3, "Nice Approach", "Author-03", 3.8)
        );

        recommendationRepository.saveAll(recommendationList);
    }

    @Test
    void testGetReviewByProductId() {
        List<ReviewEntity> recommendations = recommendationRepository.findByProductId(PRODUCT_ID_ONE);
        assertEquals(3, recommendations.size());
    }

    @Test
    void testCreateReview() {
        assertEquals(3, recommendationRepository.count());
        ReviewEntity newReviewEntity = new ReviewEntity(PRODUCT_ID_TWO, 4, "Good Product", "Author-01", 4.1);

        var savedReviewEntity = recommendationRepository.save(newReviewEntity);

        assertThat(savedReviewEntity.getContent()).isEqualTo(newReviewEntity.getContent());
        assertThat(savedReviewEntity.getRating()).isEqualTo(newReviewEntity.getRating());
        assertEquals(4, recommendationRepository.count());

    }


    @Test
    void testDeleteReview() {
        assertEquals(3, recommendationRepository.count());
        List<ReviewEntity> recommendations = recommendationRepository.findByProductId(PRODUCT_ID_ONE);
        var firstReview = recommendations.getFirst();

        recommendationRepository.delete(firstReview);

        assertEquals(2, recommendationRepository.count());
    }

    @Test
    void testUpdateReview() {

        List<ReviewEntity> recommendations = recommendationRepository.findByProductId(PRODUCT_ID_ONE);
        var firstReview = recommendations.getFirst();
        assertEquals(0, firstReview.getVersion());
        firstReview.setContent("Updated content");
        firstReview.setRating(4.11);
        firstReview.setAuthor("Updated author");
        var updatedReviewEntity = recommendationRepository.save(firstReview);

        assertThat(updatedReviewEntity.getContent()).isEqualTo("Updated content");
        assertThat(updatedReviewEntity.getRating()).isEqualTo(4.11);
        assertThat(updatedReviewEntity.getAuthor()).isEqualTo("Updated author");
        assertEquals(1, updatedReviewEntity.getVersion());
    }

    @Test
    void testDuplicateKeyError() {
        var duplicateReview = new ReviewEntity(PRODUCT_ID_ONE, 1, "Good Product", "Author-01", 3.2);
        assertThrows(DataIntegrityViolationException.class, () -> saveDuplicateReview(duplicateReview));
    }

    void saveDuplicateReview(ReviewEntity recommendation) {
        recommendationRepository.save(recommendation);
    }

}
