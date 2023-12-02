package com.mercadolibro.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Author total books count", value = "AuthorBookCountDTO")
public class AuthorBookCountDTO {
    @ApiModelProperty(notes = "Author name", example = "John Doe")
    private String authorName;

    @ApiModelProperty(notes = "Total books count", example = "10")
    private Long totalBooks;
}
