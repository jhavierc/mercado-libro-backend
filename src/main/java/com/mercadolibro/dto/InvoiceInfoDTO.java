package com.mercadolibro.dto;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
public class InvoiceInfoDTO {

    private Long id;
    private Timestamp dateCreated;
    private double total;
    private double tax;
    private int userId;
    private String bank;
    private String accountNumber;


}
