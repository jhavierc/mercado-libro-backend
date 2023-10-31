package com.mercadolibro.repository;

import com.mercadolibro.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    boolean existsByEmail(String email);
}
