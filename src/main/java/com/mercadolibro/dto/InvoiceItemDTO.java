package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class InvoiceItemDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("book_id")
    private int bookId;

    @JsonProperty("unit_price")
    private double unitPrice;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("total")
    private double total;

    @JsonProperty("invoice_id")
    private UUID invoiceId;

}
