package com.mercadolibro.service;

import com.mercadolibro.dto.FrequentQuestionCreateDTO;
import com.mercadolibro.entity.FrequentQuestion;
import com.mercadolibro.exception.ResourceNotFoundException;

import java.util.List;

public interface FrequentQuestionService {
    FrequentQuestion findById(Integer id) throws ResourceNotFoundException;
    FrequentQuestion create(FrequentQuestionCreateDTO frequentQuestion);
    FrequentQuestion update(Integer id, FrequentQuestionCreateDTO frequentQuestion) throws ResourceNotFoundException;
    void delete(Integer id) throws ResourceNotFoundException;
    List<FrequentQuestion> getAll();
}
