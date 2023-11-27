package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceItemSearchDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("unit_price")
    private double unitPrice;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("total")
    private double total;

    @JsonProperty("invoice_id")
    private int invoiceId;

    @JsonProperty("book")
    private BookDTO bookDTO;
}
