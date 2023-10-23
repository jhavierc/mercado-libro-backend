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
public class Invoice {

    @Id
    @SequenceGenerator(name = "invoice_sequence", sequenceName = "invoice_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_sequence")
    private Long id;
    @Column(name = "date_created")
    private Timestamp dateCreated;
    private double total;
    private double tax;
    @Column(name = "user_id")
    private int userId;
    private String bank;
    @Column(name = "account_number")
    private String accountNumber;

}
