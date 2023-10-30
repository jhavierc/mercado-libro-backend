package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InvoiceItemDTO {

    @JsonProperty("id")
    private Long id;

    //@JsonAlias("book_id")
    @JsonProperty("book_id")
    private int bookId;

    //@JsonAlias("unit_price")
    @JsonProperty("unit_price")
    private double unitPrice;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("total")
    private double total;

    //@JsonAlias("invoice_id")
    @JsonProperty("invoice_id")
    private int invoiceId;

}
