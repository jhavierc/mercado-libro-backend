package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Objects;

@Builder
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryRespDTO that = (CategoryRespDTO) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(status, that.status)
                && Objects.equals(description, that.description)
                && Objects.equals(imageLink, that.imageLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, description, imageLink);
    }
}