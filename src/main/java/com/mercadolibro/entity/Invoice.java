package com.mercadolibro.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "invoice")
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "VARCHAR(36)")
    @Type(type="uuid-char")
    private UUID id;

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
