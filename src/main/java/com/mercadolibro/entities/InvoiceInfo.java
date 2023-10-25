package com.mercadolibro.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Entity;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "invoice")
public class InvoiceInfo {

    @Id
    @SequenceGenerator(name = "invoice_sequence", sequenceName = "invoice_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_sequence")
    private Long id;
    @Column(name = "date_created")
    private Timestamp dateCreated;
    @Column(name = "total")
    private double total;
    @Column(name = "tax")
    private double tax;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "bank")
    private String bank;
    @Column(name = "account_number")
    private String accountNumber;

}
