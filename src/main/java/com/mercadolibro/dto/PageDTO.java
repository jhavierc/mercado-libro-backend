package com.mercadolibro.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "Model for a page of elements", value = "Page")
public class PageDTO <DTO>{

    @ApiModelProperty(value = "List of elements", required = true)
    List<DTO> content;
    @ApiModelProperty(value = "Total pages", required = true)
    Integer totalPages;
    @ApiModelProperty(value = "Total elements", required = true)
    Long totalElements;
    @ApiModelProperty(value = "Current page", required = true)
    Integer currentPage;
    @ApiModelProperty(value = "Page size", required = true)
    Integer pageSize;

    public PageDTO(List<DTO> content, Integer totalPages, Long totalElements, Integer currentPage, Integer pageSize) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }
}

