package com.mercadolibro.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Payment Response DTO", value = "PaymentRespDTO")
public class PaymentRespDTO {
    private Long id;
    private String status;
    private String detail;
}
