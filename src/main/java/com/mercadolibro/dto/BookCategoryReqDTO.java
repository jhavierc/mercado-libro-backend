package com.mercadolibro.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class BookCategoryReqDTO {
    @NotNull
    private Long id;
}
