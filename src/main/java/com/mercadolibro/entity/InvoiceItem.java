package com.mercadolibro.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "invoice_item")
public class InvoiceItem {

    @Id
    @SequenceGenerator(name = "invoice_item_sequence", sequenceName = "invoice_item_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_item_sequence")
    private Long id;

    @JsonProperty("book_id")
    @Column(name = "book_id")
    private Long bookId;

    @JsonProperty("unit_price")
    @Column(name = "unit_price")
    private double unitPrice;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "total")
    private double total;

    @JsonProperty("invoice_id")
    @Column(name = "invoice_id")
    private Long invoiceId;

}
