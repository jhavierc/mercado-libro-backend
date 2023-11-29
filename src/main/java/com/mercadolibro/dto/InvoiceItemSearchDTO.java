package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.UUID;

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
    private UUID invoiceId;

    @JsonProperty("book_id")
    private Long bookId;

    @JsonProperty("book")
    private BookDTO bookDTO;
}
