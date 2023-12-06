package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class InvoiceSearchDTO {

    @JsonProperty("id")
    private UUID id;

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

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("client")
    private ClientDTO clientDTO;

    @JsonProperty("invoice_item")
    private List<InvoiceItemSearchDTO> listInvoiceItems;

    @JsonProperty("paid")
    private Boolean paid;

    @JsonProperty("address")
    @Column(columnDefinition = "json")
    @Type(type = "json")
    private Address address;

    @JsonProperty("shipping")
    private Double shipping;

    @JsonProperty("subTotal")
    private Double subTotal;

    @JsonProperty("payment_method")
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @JsonProperty("shipping_method")
    @Column(name = "shipping_method")
    private ShippingMethod shippingMethod;

    @Getter
    private enum ShippingMethod {
        PICK_UP, CORREO_ARGENTINO
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address implements Serializable {
        private String city;
        private String street;
        private String zipCode;
        private Short number;
        private String district;
        private String state;
        private String department;
    }
}
