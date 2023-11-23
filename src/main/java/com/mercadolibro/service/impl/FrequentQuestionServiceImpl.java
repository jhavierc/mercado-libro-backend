package com.mercadolibro.service.impl;

import com.mercadolibro.dto.FrequentQuestionCreateDTO;
import com.mercadolibro.dto.mapper.FrequentQuestionMapper;
import com.mercadolibro.entity.FrequentQuestion;
import com.mercadolibro.exception.ResourceNotFoundException;
import com.mercadolibro.repository.FrequentQuestionRepository;
import com.mercadolibro.service.FrequentQuestionService;
import com.mercadolibro.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrequentQuestionServiceImpl implements FrequentQuestionService {
    private final FrequentQuestionRepository frequentQuestionRepository;
    private final FrequentQuestionMapper frequentQuestionMapper;

    @Autowired
    public FrequentQuestionServiceImpl(FrequentQuestionRepository frequentQuestionRepository, FrequentQuestionMapper frequentQuestionMapper) {
        this.frequentQuestionRepository = frequentQuestionRepository;
        this.frequentQuestionMapper = frequentQuestionMapper;
    }

    @Override
    public FrequentQuestion findById(Integer id) throws ResourceNotFoundException {
        return frequentQuestionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("FrequentQuestion with id " + id + " not found"));
    }

    @Override
    public FrequentQuestion create(FrequentQuestionCreateDTO frequentQuestion) {
        return frequentQuestionRepository.save(frequentQuestionMapper.toFrequentQuestion(frequentQuestion));
    }

    @Override
    public FrequentQuestion update(Integer id, FrequentQuestionCreateDTO frequentQuestionUpdate) throws ResourceNotFoundException {
        FrequentQuestion frequentQuestion = findById(id);
        Util.mergeObjects(frequentQuestionUpdate, frequentQuestion);
        return frequentQuestionRepository.save(frequentQuestion);
    }

    @Override
    public void delete(Integer id) throws ResourceNotFoundException {
        findById(id);
        frequentQuestionRepository.deleteById(id);
    }

    @Override
    public List<FrequentQuestion> getAll() {
        return frequentQuestionRepository.findAll(Sort.by(Sort.Direction.ASC, "order"));
    }
}
