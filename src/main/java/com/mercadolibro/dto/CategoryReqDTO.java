package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class CategoryReqDTO {
    @NotBlank
    @Size(min = 1, max = 70)
    private String name;

    @NotBlank
    @Size(min = 1, max = 10)
    private String status;

    @NotBlank
    @Size(min = 1, max = 1000)
    private String description;

    @NotBlank
    @Size(min = 1, max = 3000)
    @JsonProperty("image_link")
    private String imageLink;
}