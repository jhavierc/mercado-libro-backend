package com.mercadolibro.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Entity;
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
    @Column(name = "book_id")
    private Long bookId;
    @Column(name = "unit_price")
    private double unitPrice;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "total")
    private double total;
    @Column(name = "invoice_id")
    private Long invoiceId;

}
