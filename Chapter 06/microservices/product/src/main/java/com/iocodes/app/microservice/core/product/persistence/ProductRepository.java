package com.iocodes.app.microservice.core.product.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<ProductEntity, String> {

    Optional<ProductEntity> findByProductId(int productId);
}
