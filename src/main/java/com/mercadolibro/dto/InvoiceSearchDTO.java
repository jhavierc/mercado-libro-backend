package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class InvoiceSearchDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("date_created")
    private LocalDate dateCreated;

    @JsonProperty("total")
    private double total;

    @JsonProperty("tax")
    private double tax;

    @JsonProperty("bank")
    private String bank;

    @JsonProperty("account_number")
    private String accountNumber;

    @JsonProperty("address")
    private String  address;

    @JsonProperty("deadline")
    private String deadline;

    @JsonProperty("cardholder")
    private String cardHolder;

    @JsonProperty("expiration_date")
    private String expirationDate;

    @JsonProperty("dni")
    private Long dni;

    @JsonProperty("document_type")
    private String documentType;

    @JsonProperty("card_number")
    private String cardNumber;

    @JsonProperty("notes")
    private String notes;

    @JsonProperty("client")
    private ClientDTO clientDTO;

    @JsonProperty("invoice_item")
    private List<InvoiceItemSearchDTO> listInvoiceItems;
}
