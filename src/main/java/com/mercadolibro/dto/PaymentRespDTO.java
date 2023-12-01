package com.mercadolibro.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Payment Response DTO", value = "PaymentRespDTO")
public class PaymentRespDTO {
    @ApiModelProperty(value = "ID of the payment", required = true, example = "1")
    private Long id;

    @ApiModelProperty(value = "Status of the payment", required = true, example = "Pago aprobado")
    private String status;

    @ApiModelProperty(value = "Detail of the payment", required = true, example = "El pago ha sido aprobado exitosamente.")
    private String detail;
}
