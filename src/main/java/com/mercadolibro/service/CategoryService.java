package com.mercadolibro.service;

import com.mercadolibro.dto.CategoryReqDTO;
import com.mercadolibro.dto.CategoryRespDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<CategoryRespDTO> findAll();
    CategoryRespDTO findByID(Long id);
    CategoryRespDTO save(CategoryReqDTO category);
}
