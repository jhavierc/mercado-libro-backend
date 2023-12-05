package com.mercadolibro.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "invoice")
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "json", typeClass = JsonStringType.class)
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

    private String deadline;

    @JsonProperty("cardholder")
    @Column(name = "cardholder")
    private String cardHolder;

    @JsonProperty("expiration_date")
    @Column(name = "expiration_date")
    private String expirationDate;

    private Long dni;

    @JsonProperty("document_type")
    @Column(name = "document_type")
    private String documentType;

    @JsonProperty("card_number")
    @Column(name = "card_number")
    private String cardNumber;

    @JsonProperty("payment_method")
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @JsonProperty("address")
    @Column(name = "address", columnDefinition = "json")
    @Type(type = "json")
    private Address address;

    @JsonProperty("shipping")
    private Double shipping;

    @JsonProperty("subTotal")
    private Double subTotal;

    @JsonProperty("shipping_method")
    @Column(name = "shipping_method")
    private ShippingMethod shippingMethod;

    private String notes;

    private Boolean paid;

    @Getter
    private enum PaymentMethod {
        MERCADO_PAGO, TRANSFER
    }

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
