package com.mercadolibro.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "role")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Cacheable(false)
@ApiModel(description = "Role DTO", value = "Role")
public class AppUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "Id of the role", required = true, example = "1")
    private Integer id;
    @ApiModelProperty(value = "Name of the role", required = true, example = "ADMIN")
    private String description;
    @ApiModelProperty(value = "Status of the role", required = true, example = "ACTIVE")
    private String status;
}
