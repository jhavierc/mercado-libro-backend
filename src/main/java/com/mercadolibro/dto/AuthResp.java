package com.mercadolibro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResp {
    private String token;
    private UserDTO user;
}
