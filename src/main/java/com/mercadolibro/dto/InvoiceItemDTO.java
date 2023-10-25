package com.mercadolibro.dto;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;


@Entity
@Getter
@Setter
public class InvoiceItemDTO {


    private Long id;
    private int bookId;
    private double unitPrice;
    private int quantity;
    private double total;
    private int invoiceId;

}
