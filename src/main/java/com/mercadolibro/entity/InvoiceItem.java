package com.mercadolibro.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "invoice_item")
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "invoice_id", columnDefinition = "VARCHAR(36)")
    @Type(type="uuid-char")
    private UUID invoiceId;

}
