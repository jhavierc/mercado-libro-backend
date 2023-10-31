package com.mercadolibro.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AuthReq {
    @ApiModelProperty(value = "Email of the user", required = true, example = "juan@example.com")
    private String email;
    @ApiModelProperty(value = "Password of the user", required = true, example = "123456")
    private String password;
}
