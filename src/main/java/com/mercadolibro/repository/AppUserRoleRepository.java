package com.mercadolibro.repository;

import com.mercadolibro.entity.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRoleRepository extends JpaRepository<AppUserRole, Integer> {
    Optional<AppUserRole> findByDescription(String description);
}
