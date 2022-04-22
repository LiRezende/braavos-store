package com.lirezende.braavosstore.repositories;

import com.lirezende.braavosstore.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void shouldDeleteObjectsWhenValidId() {

        long validId = 1L;
        productRepository.deleteById(validId);
        Optional<Product> result = productRepository.findById(validId);
        Assertions.assertFalse(result.isPresent());
    }
}
