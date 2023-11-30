package com.mercadolibro.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "MercadoPago error response", value = "MercadoPagoErrorResponse")
public class MercadoPagoErrorResponse {
    private String message;
    private int status;
    private List<Cause> cause;

    @Getter
    @Setter
    public static class Cause {
        private String description;
    }
}
