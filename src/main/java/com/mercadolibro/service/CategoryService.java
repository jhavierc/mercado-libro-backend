package com.mercadolibro.service;

import com.mercadolibro.dto.CategoryReqDTO;
import com.mercadolibro.dto.CategoryRespDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryRespDTO> getAll();

    CategoryRespDTO getByID(Long id);

    CategoryRespDTO create(CategoryReqDTO categoryReqDTO);

    CategoryRespDTO update(Long id, CategoryReqDTO categoryReqDTO);

    void delete(Long id);
}
