package com.mercadolibro.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Total monthly stock", value = "MonthlyStockDTO")
public class MonthlyStockDTO {
    private String year;
    private String month;
    private Long stock;
}
