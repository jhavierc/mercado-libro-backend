package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class CategoryRespDTO {
    private Long id;
    private String name;
    private String status;
    private String description;
    @JsonProperty("image_link")
    private String imageLink;
}