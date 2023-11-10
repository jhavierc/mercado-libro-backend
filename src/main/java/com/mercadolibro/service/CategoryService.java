package com.mercadolibro.service;

import com.mercadolibro.dto.CategoryReqDTO;
import com.mercadolibro.dto.CategoryRespDTO;
import com.mercadolibro.exception.BookNotFoundException;

import java.util.List;

public interface CategoryService {

    /**
     * Retrieves all categories.
     *
     * @return A list of CategoryRespDTO containing all categories.
     * @throws BookNotFoundException If no categories are found.
     */
    List<CategoryRespDTO> findAll();

    /**
     * Finds a category by its ID.
     *
     * @param id The ID of the category to retrieve.
     * @return The CategoryRespDTO representing the category with the given ID.
     * @throws BookNotFoundException If the category with the specified ID is not found.
     */
    CategoryRespDTO findByID(Long id);

    /**
     * Saves a new category.
     *
     * @param category The CategoryReqDTO object representing the category to be saved.
     * @return The CategoryRespDTO of the newly saved category.
     */
    CategoryRespDTO save(CategoryReqDTO category);

    /**
     * Updates an existing category.
     * @param id The CategoryReqDTO object representing the category to be updated.
     * @param category The CategoryReqDTO object representing the category to be updated.
     * @return The CategoryRespDTO of the updated category.
     */
    CategoryRespDTO update(Long id, CategoryReqDTO category);
}
