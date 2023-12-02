package com.mercadolibro.dto;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Total sales by payment type", value = "PaymentTypeSaleDTO")
public interface PaymentTypeSaleDTO {
    PaymentMethod getPayment_type();
    Long getSales();
}

