package com.mercadolibro.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("date_created")
    @Column(name = "date_created")
    private LocalDate dateCreated;

    @Column(name = "total")
    private double total;

    @Column(name = "tax")
    private double tax;

    @JsonProperty("user_id")
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "bank")
    private String bank;

    @JsonProperty("account_number")
    @Column(name = "account_number")
    private String accountNumber;

}
