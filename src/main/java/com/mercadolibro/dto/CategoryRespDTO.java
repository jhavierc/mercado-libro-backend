package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@ApiModel(description = "Category Response DTO", value = "CategoryResp")
public class CategoryRespDTO {
    @ApiModelProperty(value = "Id of the category", required = true, example = "1")
    private Long id;

    @ApiModelProperty(value = "Name of the category", required = true, example = "Novela")
    private String name;

    @ApiModelProperty(value = "Status of the category", required = true, example = "ACTIVE")
    private String status;

    @ApiModelProperty(value = "Description of the category", required = true, example = "La novela desarrolla tramas complejas, personajes profundos y entornos detallados. Explora narrativas extensas y multifacéticas, permitiendo una inmersión profunda en mundos ficticios.")
    private String description;

    @JsonProperty("image_link")
    @ApiModelProperty(value = "Image link of the category", required = true, example = "https://example.com")
    private String imageLink;
}