package com.mercadolibro.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user")
@Data
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @NotNull
    @Size(min = 2, max = 120)
    String name;

    @NotNull
    @Size(min = 2, max = 120)
    @Column(name = "last_name")
    String lastName;

    @Email
    @NotNull
    String email;

    @NotNull
    @Size(min = 6)
    String password;

    String status;

    @Column(name = "date_created")
    LocalDateTime dateCreated;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<AppUserRole> roles;

}
