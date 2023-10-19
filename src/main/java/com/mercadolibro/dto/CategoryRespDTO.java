package com.mercadolibro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class CategoryRespDTO {
    private Long id;
    private String name;
    private String status;
    private String description;
    private String imageLink;
}
