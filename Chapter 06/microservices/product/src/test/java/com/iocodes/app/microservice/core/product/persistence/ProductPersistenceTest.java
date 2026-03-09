package com.iocodes.app.microservice.core.product.persistence;


import com.iocodes.app.microservice.core.product.util.MongoDbTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.ASC;


@DataMongoTest
class   ProductPersistenceTest extends MongoDbTestBase {



    final int PRODUCT_ID_ONE = 1;
    final int PRODUCT_ID_TWO = 2;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUpDb () {
        productRepository.deleteAll();
        ProductEntity product = new ProductEntity(PRODUCT_ID_ONE, 1, "Product-1");

        productRepository.save(product);
    }

    @Test
    void testGetProductByProductId() {
        Optional<ProductEntity> optionalProduct = productRepository.findByProductId(PRODUCT_ID_ONE);
        assertTrue(optionalProduct.isPresent());

        ProductEntity product = optionalProduct.get();
        assertNotNull(product);

        assertEquals(PRODUCT_ID_ONE, product.getProductId());
        assertEquals(1, product.getWeight());
        assertEquals("Product-1", product.getName());

        assertEquals(1, productRepository.count());
    }

    @Test
    void testCreateProduct() {
        assertEquals(1, productRepository.count());
        ProductEntity newProductEntity = new ProductEntity(PRODUCT_ID_TWO, 4, "Product-2");

        var savedProductEntity = productRepository.save(newProductEntity);

        assertThat(savedProductEntity.getVersion()).isEqualTo(newProductEntity.getVersion());
        assertThat(savedProductEntity.getWeight()).isEqualTo(newProductEntity.getWeight());
        assertEquals(2, productRepository.count());

    }


    @Test
    void testDeleteProduct() {
        assertEquals(1, productRepository.count());
        Optional<ProductEntity> optionalProduct = productRepository.findByProductId(PRODUCT_ID_ONE);
        assertTrue(optionalProduct.isPresent());

        ProductEntity product = optionalProduct.get();
        assertNotNull(product);


        productRepository.delete(product);

        assertEquals(0, productRepository.count());
    }

    @Test
    void testUpdateProduct() {

        Optional<ProductEntity> optionalProduct = productRepository.findByProductId(PRODUCT_ID_ONE);
        assertTrue(optionalProduct.isPresent());
        var product = optionalProduct.get();
        assertEquals(0, product.getVersion());
        product.setName("Updated Product-1");
        product.setWeight(2);
        var updatedProductEntity = productRepository.save(product);

        assertThat(updatedProductEntity.getName()).isEqualTo("Updated Product-1");
        assertThat(updatedProductEntity.getWeight()).isEqualTo(2);
        assertEquals(1, product.getVersion());
    }

    @Test
    void testDuplicateKeyError() {
        var duplicateProduct = new ProductEntity(PRODUCT_ID_ONE, 1, "New Product-1 entity");
        assertThrows(DuplicateKeyException.class, () -> saveProduct(duplicateProduct));
    }


    @Test
    void testOptimisticLockError() {
        var product = productRepository.findByProductId(PRODUCT_ID_ONE).orElseThrow();
        var secondProduct = productRepository.findByProductId(PRODUCT_ID_ONE).orElseThrow();

        // First update succeeds
        product.setName("Updated Product-1");
        product.setWeight(2);
        productRepository.save(product);

        // Second update on stale version should fail
        assertThrows(OptimisticLockingFailureException.class, () -> saveProduct(secondProduct));

        // Assert DB reflects only the first successful save
        var savedProduct = productRepository.findByProductId(PRODUCT_ID_ONE).orElseThrow();
        assertEquals("Updated Product-1", savedProduct.getName());
        assertEquals(2, savedProduct.getWeight());
        assertEquals(1, savedProduct.getVersion());
    }


    @Test
    void testPaging() {
        productRepository.deleteAll();

        List<ProductEntity> productEntities = IntStream.rangeClosed(1000, 1010)
                .mapToObj(i -> new ProductEntity(i, (i + 0.5), "Name - " + i))
                .toList();

        productRepository.saveAll(productEntities);

        Pageable nextPage = PageRequest.of(0, 4, ASC, "productId");

        nextPage = testNextPage(nextPage, new int[]{
            1000, 1001, 1002, 1003
        }, true);
        nextPage = testNextPage(nextPage, new int[]{
            1004, 1005, 1006, 1007
        }, true);
        nextPage = testNextPage(nextPage, new int[]{
            1008, 1009, 1010
        }, false);

    }

    Pageable testNextPage(Pageable page, int[] productIds, boolean expected){
        int i = 0;
        Page<ProductEntity> pagedProduct = productRepository.findAll(page);
        List<ProductEntity> productList = pagedProduct.getContent();

        for(int id: productIds) {
            assertEquals(id, productList.get(i).getProductId());
            i++;
        }
        var nextPage = pagedProduct.nextPageable();

        assertEquals(expected, (page.getPageNumber() < pagedProduct.getTotalPages() - 1));
        return pagedProduct.nextPageable();
    }


    void saveProduct(ProductEntity product) {
        productRepository.save(product);
    }

}
