package com.mercadolibro.repository;

import com.mercadolibro.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    boolean existsByEmail(String email);
    Optional<AppUser> findByEmail(String email);
}
