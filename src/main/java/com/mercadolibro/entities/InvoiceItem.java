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
    private int bookId;
    @Column(name = "unit_price")
    private double unitPrice;
    private int quantity;
    private double total;
    @Column(name = "invoice_id")
    private int invoiceId;

}
