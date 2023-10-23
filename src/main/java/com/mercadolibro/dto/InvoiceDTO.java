package com.mercadolibro.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Entity;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class InvoiceDTO {

    private Long id;
    private Timestamp dateCreated;
    private double total;
    private double tax;
    private int userId;
    private String bank;
    private String accountNumber;

}
