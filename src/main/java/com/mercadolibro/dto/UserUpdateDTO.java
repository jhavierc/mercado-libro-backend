package com.mercadolibro.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.mercadolibro.entity.AppUserRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "User DTO for updating user information", parent = UserDTO.class, value = "UserUpdate")
public class UserUpdateDTO {

    @Size(min = 2, max = 120)
    @ApiModelProperty(value = "Name of the user", required = false, example = "Juan")
    String name;

    @Size(min = 2, max = 120)
    @Column(name = "last_name")
    @JsonAlias("last_name")
    @ApiModelProperty(value = "Last name of the user", required = false, example = "Gutierrez")
    String lastName;

    @Email
    @ApiModelProperty(value = "Email of the user", required = false, example = "juan@example.com")
    String email;

    @Size(min = 6)
    @ApiModelProperty(value = "Password of the user", required = false, example = "123456")
    String password;

    @ApiModelProperty(value = "Status of the user", required = false, example = "ACTIVE", notes = "ACTIVE, INACTIVE")
    String status;

    @ApiModelProperty(value = "roles of the user", required = false)
    private List<AppUserRole> roles;

    @AssertTrue(message = "Status must be ACTIVE or INACTIVE")
    private boolean isStatusValid() {
        return this.status.equals("ACTIVE") || this.status.equals("INACTIVE");
    }
}
