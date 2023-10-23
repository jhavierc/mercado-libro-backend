package com.mercadolibro.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "role")
@Data
public class AppUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    private String status;
}
