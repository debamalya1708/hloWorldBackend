package com.dp.hloworld.controller;

import com.dp.hloworld.model.Category;
import com.dp.hloworld.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/category")
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @PostMapping(value="/new")
    public void save(@RequestBody Category category) {
        categoryRepository.save(category);
    }

    @GetMapping("/all")
    public List<Category> get(){
        return categoryRepository.findAll();
    }
}
