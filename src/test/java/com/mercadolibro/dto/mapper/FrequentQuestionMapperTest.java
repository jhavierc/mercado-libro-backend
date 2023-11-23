package com.mercadolibro.dto.mapper;

import com.mercadolibro.dto.FrequentQuestionCreateDTO;
import com.mercadolibro.entity.FrequentQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FrequentQuestionMapperTest {
    FrequentQuestionMapper mapper = FrequentQuestionMapper.INSTANCE;
    List<FrequentQuestion> frequentQuestionList = new ArrayList<>();
    List<FrequentQuestionCreateDTO> frequentQuestionDTOList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        frequentQuestionList.add(new FrequentQuestion(1, "Pregunta 1", "Respuesta 1", 1));
        frequentQuestionDTOList.add(new FrequentQuestionCreateDTO("Pregunta 1", "Respuesta 1", 1));
    }

    @Test
    void shouldMapFrequentQuestionCreateToEntity() {
        FrequentQuestion frequentQuestion = frequentQuestionList.get(0);
        FrequentQuestionCreateDTO frequentQuestionCreateDTO = frequentQuestionDTOList.get(0);
        frequentQuestion.setId(null);

        assertEquals(frequentQuestion, mapper.toFrequentQuestion(frequentQuestionCreateDTO));
    }

}