package com.iocodes.app.microservice.core.recommendation.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationRepository extends MongoRepository<RecommendationEntity, String> {

    @Transactional(readOnly = true)
    List<RecommendationEntity> findByProductId(int productId);
}
