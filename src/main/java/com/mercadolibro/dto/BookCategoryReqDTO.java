package com.mercadolibro.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Book Category Request DTO", value = "BookCategoryReq")
public class BookCategoryReqDTO {
    @NotNull
    @ApiModelProperty(value = "Id of the category", required = true, example = "1")
    private Long id;
}
