package com.mercadolibro.repositories;

import com.mercadolibro.entities.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRoleRepository extends JpaRepository<AppUserRole, Integer> {
    Optional<AppUserRole> findByDescription(String description);
}
