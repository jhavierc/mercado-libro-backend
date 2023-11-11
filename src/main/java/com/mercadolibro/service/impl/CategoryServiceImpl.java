package com.mercadolibro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.*;
import com.mercadolibro.entity.Category;
import com.mercadolibro.exception.BookNotFoundException;
import com.mercadolibro.exception.CategoryNotFoundException;
import com.mercadolibro.repository.CategoryRepository;
import com.mercadolibro.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ObjectMapper mapper;

    private static final String CATEGORY_NOT_FOUND_ERROR_FORMAT = "Could not find category with ID #%d.";
    private static final String NO_CATEGORIES_TO_SHOW_ERROR_FORMAT = "There is no category to show.";

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ObjectMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CategoryRespDTO> findAll() {
        List<Category> searched = categoryRepository.findAll();
        if (!searched.isEmpty()) {
            return searched.stream().map(category -> mapper.convertValue(category, CategoryRespDTO.class))
                    .collect(Collectors.toList());
        }
        throw new BookNotFoundException(NO_CATEGORIES_TO_SHOW_ERROR_FORMAT);
    }

    @Override
    public CategoryRespDTO findByID(Long id) {
        Optional<Category> searched = categoryRepository.findById(id);
        if (searched.isPresent()) {
            return mapper.convertValue(searched.get(), CategoryRespDTO.class);
        }
        throw new BookNotFoundException(String.format(CATEGORY_NOT_FOUND_ERROR_FORMAT, id));
    }

    @Override
    public CategoryRespDTO save(CategoryReqDTO category) {
        Category saved = categoryRepository.save(mapper.convertValue(category, Category.class));
        return mapper.convertValue(saved, CategoryRespDTO.class);
    }

    @Override
    public CategoryRespDTO update(Long id, CategoryReqDTO category){
        if (categoryRepository.existsById(id)) {
            Category toUpdate = mapper.convertValue(category, Category.class);
            toUpdate.setId(id);
            Category updated = categoryRepository.save(toUpdate);
            return mapper.convertValue(updated, CategoryRespDTO.class);
        }

        throw new CategoryNotFoundException(String.format(CATEGORY_NOT_FOUND_ERROR_FORMAT, id));
    }
}
