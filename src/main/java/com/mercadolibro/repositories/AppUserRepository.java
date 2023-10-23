package com.mercadolibro.repositories;

import com.mercadolibro.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    boolean existsByEmail(String email);
}
