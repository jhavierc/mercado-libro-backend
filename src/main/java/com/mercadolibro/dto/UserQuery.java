package com.mercadolibro.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;

/**
 * Model for make a query to the user
 * Each field is optional, so you can make a query with only one field or with all of them
 * If you don't specify the orderDirection or orderBy, the default values are ASC and ID respectively
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Model for make a query to the user", value = "UserQuery")
@Builder
public class UserQuery {
    @ApiModelProperty(value = "Name of the user", required = false, example = "Juan")
    String name;
    @ApiModelProperty(value = "Last name of the user", required = false, example = "Gutierrez")
    String lastName;
    @ApiModelProperty(value = "Email of the user", required = false, example = "juan@example.com")
    @Email
    String email;
    @ApiModelProperty(value = "Status of the user", required = false, example = "ACTIVE")
    String status;
    @ApiModelProperty(value = "Direction of the sort", required = false, example = "ASC",
            notes = "Only ASC or DESC are valid values")
    Sort.Direction orderDirection = Sort.Direction.ASC;
    @ApiModelProperty(value = "Order by of the user", required = false, example = "NAME",
            notes = "Only ID, NAME, LAST_NAME, EMAIL or STATUS are valid values")
    OrderBy orderBy = OrderBy.ID;

    @Getter
    public enum OrderBy {
        ID("id"), NAME("name"), LAST_NAME("lastName"), EMAIL("email"), STATUS("status");

        private String value;

        OrderBy(String value) {
            this.value = value;
        }


    }


}

