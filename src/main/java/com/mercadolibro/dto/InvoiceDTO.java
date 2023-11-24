package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;

@Getter
@Setter
public class InvoiceDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("date_created")
    private LocalDate dateCreated;

    @JsonProperty("total")
    private double total;

    @JsonProperty("tax")
    private double tax;

    @JsonProperty("user_id")
    private int userId;

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

}
