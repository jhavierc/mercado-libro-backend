package com.mercadolibro.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Payment Request DTO", value = "PaymentReqDTO")
public class PaymentReqDTO {
    private String token;
    private String issuerId;
    private String paymentMethodId;
    private BigDecimal transactionAmount;
    private Integer installments;
    private String description;
    private Payer payer;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Payer {
        private String firstName;
        private String email;
        private Identification identification;

        @Getter
        @Setter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Identification {
            private String type;
            private String number;
        }
    }
}