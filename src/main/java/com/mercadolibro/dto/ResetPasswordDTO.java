package com.mercadolibro.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model for changing an user's password")
public class ResetPasswordDTO {
    private String password;
    private String code;
}
