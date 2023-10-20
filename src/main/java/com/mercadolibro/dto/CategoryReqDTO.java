package com.mercadolibro.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class CategoryReqDTO {
    @NotNull
    private Long id;
}
