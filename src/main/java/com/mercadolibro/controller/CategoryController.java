package com.mercadolibro.controller;

import com.mercadolibro.dto.CategoryReqDTO;
import com.mercadolibro.dto.CategoryRespDTO;
import com.mercadolibro.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@CrossOrigin(origins = "*")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryRespDTO> save(@RequestBody CategoryReqDTO category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryRespDTO> findByID(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findByID(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoryRespDTO>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }
}
