package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
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


}
