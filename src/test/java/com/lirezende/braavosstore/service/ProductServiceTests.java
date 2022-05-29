package com.lirezende.braavosstore.service;

import com.lirezende.braavosstore.dto.ProductDTO;
import com.lirezende.braavosstore.entities.Category;
import com.lirezende.braavosstore.entities.Product;
import com.lirezende.braavosstore.factory.Factory;
import com.lirezende.braavosstore.repositories.CategoryRepository;
import com.lirezende.braavosstore.repositories.ProductRepository;
import com.lirezende.braavosstore.services.ProductService;
import com.lirezende.braavosstore.services.exceptions.DatabaseException;
import com.lirezende.braavosstore.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    private Long validId;
    private Long invalidId;
    private Long relatedId;
    private Product product;
    private ProductDTO productDTO;
    private Category category;
    private PageImpl<Product> page;

    @BeforeEach
    void setUp() throws Exception {

        validId = 5L;
        invalidId = 28000L;
        relatedId = 4L;
        category = Factory.createCategory();
        product = Factory.createProduct();
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(product));

        Mockito.doNothing().when(productRepository).deleteById(validId);

        Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(invalidId);

        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(relatedId);

        Mockito.when(productRepository.save(any())).thenReturn(product);

        Mockito.when(productRepository.findById(validId)).thenReturn(Optional.of(product));

        Mockito.when(productRepository.findById(invalidId)).thenReturn(Optional.empty());

        Mockito.when(productRepository.find(any(), any(), any())).thenReturn(page);

        Mockito.when(productRepository.findAll((Pageable) any())).thenReturn(page);

        Mockito.when(productRepository.getOne(validId)).thenReturn(product);

        Mockito.when(productRepository.getOne(invalidId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(categoryRepository.getOne(validId)).thenReturn(category);

        Mockito.when(categoryRepository.getOne(invalidId)).thenThrow(EntityNotFoundException.class);
    }

    @Test
    public void deleteShouldDoNothingWhenValidId() {

        Assertions.assertDoesNotThrow(() -> {
            productService.delete(validId);
        });
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(validId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenInvalidId() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(invalidId);
        });
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(invalidId);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenRelatedId() {

        Assertions.assertThrows(DatabaseException.class, () -> {
            productService.delete(relatedId);
        });
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(relatedId);
    }

    @Test
    public void findAllPagedShouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 20);
        Page<ProductDTO> result = productService.findAllPaged(0L, "", pageable);
        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldReturnProductDtoWhenValidId() {

       ProductDTO result = productService.findById(validId);
       Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenInvalidId() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.findById(invalidId);
        });
    }

    @Test
    public void updateShouldReturnProductDtoWhenValidId() {

        ProductDTO result = productService.update(validId, productDTO);
        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenInvalidId() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.update(invalidId, productDTO);
        });
    }
}
