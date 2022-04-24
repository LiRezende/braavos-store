package com.lirezende.braavosstore.repositories;

import com.lirezende.braavosstore.entities.Product;
import com.lirezende.braavosstore.factory.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    private long validId;
    private long invalidId;
    private long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        validId = 1L;
        invalidId = 50000L;
        countTotalProducts = 25L;
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull() {

        Product product = Factory.createProduct();

        product.setId(null);
        product = productRepository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    public void shouldDeleteObjectsWhenValidId() {

        productRepository.deleteById(validId);
        Optional<Product> result = productRepository.findById(validId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldThrowExceptionWhenInvalidId() {

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            productRepository.deleteById(invalidId);
        });
    }

    @Test
    public void findByIdShouldReturnNonEmptyOptionalWhenValidId() {

        Optional<Product> result = productRepository.findById(validId);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void findByIdShouldReturnEmptyOptionalWhenInvalidId() {

        Optional<Product> result = productRepository.findById(invalidId);
        Assertions.assertTrue(result.isEmpty());
    }
}
