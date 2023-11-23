package com.mercadolibro.dto.mapper;

import com.mercadolibro.dto.FrequentQuestionCreateDTO;
import com.mercadolibro.entity.FrequentQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FrequentQuestionMapper {
    FrequentQuestionMapper INSTANCE = Mappers.getMapper(FrequentQuestionMapper.class);

    FrequentQuestion toFrequentQuestion(FrequentQuestionCreateDTO frequentQuestionCreateDTO);
}
