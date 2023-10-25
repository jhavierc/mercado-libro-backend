package com.mercadolibro.dto;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
public class InvoiceDTO {

    private Long id;
    private LocalDate dateCreated;
    private double total;
    private double tax;
    private int userId;
    private String bank;
    private String accountNumber;


}
