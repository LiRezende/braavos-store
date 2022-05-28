package com.lirezende.braavosstore.services;

import com.lirezende.braavosstore.dto.CategoryDTO;
import com.lirezende.braavosstore.dto.ProductDTO;
import com.lirezende.braavosstore.entities.Category;
import com.lirezende.braavosstore.entities.Product;
import com.lirezende.braavosstore.repositories.CategoryRepository;
import com.lirezende.braavosstore.repositories.ProductRepository;
import com.lirezende.braavosstore.services.exceptions.DatabaseException;
import com.lirezende.braavosstore.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Long categoryId, String name, Pageable pageable) {
        List<Category> categories = (categoryId == 0) ? null :
                Arrays.asList(categoryRepository.getOne(categoryId));
        Page<Product> list = repository.find(categories, name, pageable);
        return list.map(x -> new ProductDTO(x));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> obj = repository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Não existem resultados para esta busca."));
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        dtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = repository.getOne(id);
            dtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Categoria #" + id + " não encontrada.");
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch(EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id não encontrado #" + id);
        } catch(DataIntegrityViolationException e) {
            throw new DatabaseException("Violação de integridade.");

        }
    }

    private void dtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
        entity.setDate(dto.getDate());

        entity.getCategories().clear();
        for(CategoryDTO catDto : dto.getCategories()) {
            Category category = categoryRepository.getOne(catDto.getId());
            entity.getCategories().add(category);
        }
    }
}
