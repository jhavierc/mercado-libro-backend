package com.mercadolibro.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResp {
    @ApiModelProperty(value = "Token of the user", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFuQGV4YW1wbGUuY29tIiwicm9sZXMiOlsiUk9MRV9VU0")
    private String token;
    @ApiModelProperty(value = "User")
    private UserDTO user;
}
