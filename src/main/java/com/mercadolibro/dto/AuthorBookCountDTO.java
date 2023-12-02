package com.mercadolibro.dto;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Author total books count", value = "AuthorBookCountDTO")
public interface AuthorBookCountDTO {
    String getAuthor();

    Integer getTotal_books();
}
