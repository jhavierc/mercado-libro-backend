package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.mercadolibro.entities.AppUserRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "User DTO", value = "User")
public class UserDTO {
    @ApiModelProperty(value = "Id of the user", required = true, example = "1")
    Integer id;

    @NotNull
    @Size(min = 2, max = 120)
    @ApiModelProperty(value = "Name of the user", required = true, example = "Juan")
    String name;

    @NotNull
    @Size(min = 2, max = 120)
    @Column(name = "last_name")
    @JsonAlias("last_name")
    @ApiModelProperty(value = "Last name of the user", required = true, example = "Gutierrez")
    String lastName;

    @Email
    @NotNull
    @ApiModelProperty(value = "Email of the user", required = true, example = "juan@example.com")
    String email;

    @ApiModelProperty(value = "Status of the user", required = false, example = "ACTIVE")
    String status;

    @Column(name = "date_created")
    @JsonAlias("date_created")
    @ApiModelProperty(value = "Date of creation of the user", required = false, example = "2021-07-01T00:00:00")
    LocalDateTime dateCreated;

    private List<AppUserRole> roles;
}
