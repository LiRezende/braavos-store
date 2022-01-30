package com.lirezende.braavosstore.resources;

import com.lirezende.braavosstore.entities.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    @GetMapping
    public ResponseEntity<List<Category>> findAll() {
        List<Category> list = new ArrayList<>();
        list.add(new Category(1L, "Books"));
        list.add(new Category(2L, "Games"));
        list.add(new Category(3L, "Kids"));
        list.add(new Category(4L, "Pets"));
        list.add(new Category(5L, "Cars"));
        return ResponseEntity.ok().body(list);
    }
}
