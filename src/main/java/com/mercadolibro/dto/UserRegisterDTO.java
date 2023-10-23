package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    @NotNull
    @Size(min = 2, max = 120)
    String name;

    @NotNull
    @Size(min = 2, max = 120)
    @JsonAlias("last_name")
    String lastName;
    @Email
    @NotNull
    String email;
    @NotNull
    @Size(min = 6)
    String password;
}
