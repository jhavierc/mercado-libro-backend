package com.mercadolibro.dto;

import lombok.Data;

@Data
public class AuthReq {
    private String email;
    private String password;
}
