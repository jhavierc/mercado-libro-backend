package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.mercadolibro.entities.AppUserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    Integer id;

    @NotNull
    @Size(min = 2, max = 120)
    String name;

    @NotNull
    @Size(min = 2, max = 120)
    @Column(name = "last_name")
    @JsonAlias("last_name")
    String lastName;

    @Email
    @NotNull
    String email;

    String status;

    @Column(name = "date_created")
    @JsonAlias("date_created")
    LocalDateTime dateCreated;

    private List<AppUserRole> roles;
}
