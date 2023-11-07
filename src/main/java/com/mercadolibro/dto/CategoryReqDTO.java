package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ApiModel(description = "Category Request DTO", value = "CategoryReq")
public class CategoryReqDTO {
    @NotBlank
    @Size(min = 1, max = 70)
    @ApiModelProperty(value = "Name of the category", required = true, example = "Novela")
    private String name;

    @NotBlank
    @Size(min = 1, max = 10)
    @ApiModelProperty(value = "Status of the category", required = true, example = "ACTIVE")
    private String status;

    @NotBlank
    @Size(min = 1, max = 1000)
    @ApiModelProperty(value = "Description of the category", required = true, example =
            "La novela desarrolla tramas complejas, personajes profundos y entornos detallados. Explora narrativas " +
                    "extensas y multifacéticas, permitiendo una inmersión profunda en mundos ficticios.")
    private String description;

    @NotBlank
    @Size(min = 1, max = 3000)
    @JsonProperty("image_link")
    @ApiModelProperty(value = "Image link of the category", required = true, example = "https://example.com")
    private String imageLink;
}
