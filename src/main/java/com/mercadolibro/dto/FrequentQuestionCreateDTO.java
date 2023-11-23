package com.mercadolibro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FrequentQuestionCreateDTO {
    @NotNull
    @NotEmpty
    private String question;
    @NotNull
    @NotEmpty
    private String answer;
    @NotNull
    private Integer order;
}
