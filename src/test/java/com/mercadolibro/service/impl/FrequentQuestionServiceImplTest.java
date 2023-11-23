package com.mercadolibro.service.impl;

import com.mercadolibro.dto.FrequentQuestionCreateDTO;
import com.mercadolibro.dto.mapper.FrequentQuestionMapper;
import com.mercadolibro.entity.FrequentQuestion;
import com.mercadolibro.exception.ResourceNotFoundException;
import com.mercadolibro.repository.FrequentQuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FrequentQuestionServiceImplTest {

    @Mock
    private FrequentQuestionRepository frequentQuestionRepository;
    @Spy
    private FrequentQuestionMapper frequentQuestionMapper = FrequentQuestionMapper.INSTANCE;
    @InjectMocks
    private FrequentQuestionServiceImpl frequentQuestionService;

    List<FrequentQuestion> frequentQuestions = new ArrayList<>();

    @BeforeEach()
    void setup() {
        frequentQuestions.add(new FrequentQuestion(1, "Pregunta 1", "Respuesta 1", 1));
        frequentQuestions.add(new FrequentQuestion(2, "Pregunta 2", "Respuesta 2", 2));
        frequentQuestions.add(new FrequentQuestion(3, "Pregunta 3", "Respuesta 3", 3));
    }


    @Test
    void shouldFindByID() throws ResourceNotFoundException {
        // Given
        FrequentQuestion frequentQuestion = frequentQuestions.get(0);
        Integer id = frequentQuestion.getId();
        // When
        when(frequentQuestionRepository.findById(id)).thenReturn(Optional.of(frequentQuestion));
        FrequentQuestion frequentQuestionFound = frequentQuestionService.findById(id);
        // Then
        assertEquals(frequentQuestion, frequentQuestionFound);
    }

    @Test
    void findByIdShouldThrowResourceNotFoundException() {
        // Given
        Integer id = 1;
        // When
        when(frequentQuestionRepository.findById(id)).thenReturn(Optional.empty());
        // Then
        assertThrows(ResourceNotFoundException.class, () -> frequentQuestionService.findById(id));
    }

    @Test
    void shouldCreate() {
        // Given
        FrequentQuestion frequentQuestion = frequentQuestions.get(0);
        FrequentQuestionCreateDTO frequentQuestionCreateDTO = new FrequentQuestionCreateDTO(frequentQuestion.getQuestion(), frequentQuestion.getAnswer(), frequentQuestion.getOrder());
        // When
        when(frequentQuestionRepository.save(any())).thenReturn(frequentQuestion);
        FrequentQuestion frequentQuestionCreated = frequentQuestionService.create(frequentQuestionCreateDTO);
        // Then
        assertEquals(frequentQuestion, frequentQuestionCreated);
    }

    @Test
    void shouldUpdate() throws ResourceNotFoundException {
        // Given
        FrequentQuestion frequentQuestion = frequentQuestions.get(0);
        FrequentQuestionCreateDTO frequentQuestionCreateUpdated = new FrequentQuestionCreateDTO(frequentQuestion.getQuestion(), frequentQuestion.getAnswer() + " answer edited", frequentQuestion.getOrder());

        //WHEN
        when(frequentQuestionRepository.findById(frequentQuestion.getId())).thenReturn(Optional.of(frequentQuestion));
        when(frequentQuestionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        FrequentQuestion frequentQuestionUpdate = frequentQuestionService.update(frequentQuestion.getId(), frequentQuestionCreateUpdated);

        //THEN
        assertEquals(frequentQuestion.getId(), frequentQuestionUpdate.getId());
        assertEquals(frequentQuestionCreateUpdated.getAnswer(), frequentQuestionUpdate.getAnswer());
        assertEquals(frequentQuestionCreateUpdated.getOrder(), frequentQuestionUpdate.getOrder());
        assertEquals(frequentQuestionCreateUpdated.getQuestion(), frequentQuestionUpdate.getQuestion());
    }

    @Test
    void updateShouldThrowResourceNotFoundException() {
        // Given
        Integer id = 1;
        FrequentQuestionCreateDTO frequentQuestionCreateUpdated = new FrequentQuestionCreateDTO("Pregunta 1", "Respuesta 1", 1);

        // When
        when(frequentQuestionRepository.findById(id)).thenReturn(Optional.empty());

        // Then
        assertThrows(ResourceNotFoundException.class, () -> frequentQuestionService.update(id, frequentQuestionCreateUpdated));
    }

    @Test
    void shouldDelete() throws ResourceNotFoundException {
        // Given
        FrequentQuestion frequentQuestion = frequentQuestions.get(0);
        Integer id = frequentQuestion.getId();
        // When
        when(frequentQuestionRepository.findById(id)).thenReturn(Optional.of(frequentQuestion));
        frequentQuestionService.delete(id);
        // Then
        assertTrue(true);
    }

}