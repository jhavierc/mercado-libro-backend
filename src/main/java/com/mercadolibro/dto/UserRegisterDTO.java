package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "User DTO for registering a new user", parent = UserDTO.class, value = "UserRegister")
public class UserRegisterDTO {
    @NotNull
    @Size(min = 2, max = 120)
    @ApiModelProperty(value = "Name of the user", required = true, example = "Juan")
    String name;

    @NotNull
    @Size(min = 2, max = 120)
    @JsonAlias("last_name")
    @ApiModelProperty(value = "Last name of the user", required = true, example = "Gutierrez")
    String lastName;
    @Email
    @NotNull
    @ApiModelProperty(value = "Email of the user", required = true, example = "juan@example.com")
    String email;
    @NotNull
    @Size(min = 6)
    @ApiModelProperty(value = "Password of the user", required = true, example = "123456")
    String password;
}
