package com.lirezende.braavosstore.services;

import com.lirezende.braavosstore.entities.Category;
import com.lirezende.braavosstore.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;
     public List<Category> findAll() {
         return repository.findAll();
     }

}
