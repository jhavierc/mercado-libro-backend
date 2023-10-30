package com.mercadolibro.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "invoice")
public class Invoice {

    @Id
    @SequenceGenerator(name = "invoice_sequence", sequenceName = "invoice_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_sequence")
    private Long id;
    @Column(name = "date_created")
    private LocalDate dateCreated;
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
