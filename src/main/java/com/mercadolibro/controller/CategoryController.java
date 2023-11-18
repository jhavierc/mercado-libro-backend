package com.mercadolibro.controller;

import com.mercadolibro.dto.CategoryReqDTO;
import com.mercadolibro.dto.CategoryRespDTO;
import com.mercadolibro.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(value = "/api/category")
@Validated
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT } )
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ApiOperation(value = "Create a new category", notes = "Returns the created category")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Category created successfully", response = CategoryRespDTO.class),
                    @ApiResponse(code = 400, message = "Bad request"),
            }
    )
    public ResponseEntity<CategoryRespDTO> save(@RequestBody @Valid CategoryReqDTO category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.save(category));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a category by ID", notes = "Returns category by ID")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Category returned successfully", response = CategoryRespDTO.class),
                    @ApiResponse(code = 404, message = "Category not found"),
            }
    )
    public ResponseEntity<CategoryRespDTO> findByID(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(categoryService.findByID(id));
    }

    @GetMapping
    @ApiOperation(value = "Gets all categories", notes = "Returns all categories")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Categories returned successfully", response = CategoryRespDTO.class),
                    @ApiResponse(code = 404, message = "No categories to show"),
            }
    )
    public ResponseEntity<List<CategoryRespDTO>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates an existing category", notes = "Returns the updated category")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Category updated successfully", response = CategoryRespDTO.class),
                    @ApiResponse(code = 404, message = "Category not found"),
            }
    )
    public ResponseEntity<CategoryRespDTO> update(@PathVariable @Positive Long id, @Valid @RequestBody CategoryReqDTO category) {
        return ResponseEntity.ok(categoryService.update(id, category));
    }
}
