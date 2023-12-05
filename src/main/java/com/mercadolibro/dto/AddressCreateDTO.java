package com.mercadolibro.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel(description = "Address Create DTO", value = "AddressCreate")
@Data
@Builder
public class AddressCreateDTO {
    @NotNull
    @ApiModelProperty(value = "Street of the address", required = true, example = "Av. Siempre Viva")
    private String street;

    @ApiModelProperty(value = "Number of the address", required = true, example = "742")
    @NotNull
    private Integer number;

    @ApiModelProperty(value = "City of the address", required = true, example = "Buenos Aires")
    @NotNull
    private String city;

    @ApiModelProperty(value = "State of the address", required = true, example = "Buenos Aires")
    @NotNull
    private String state;

    @ApiModelProperty(value = "Zip code of the address", required = true, example = "3100")
    @NotNull
    private String zipCode;

    @ApiModelProperty(value = "district of the address", required = false, example = "Barrio Norte")
    private String district;

    @ApiModelProperty(value = "department of the address", required = false, example = "2")
    private String department;
}
