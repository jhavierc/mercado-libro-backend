package com.mercadolibro.repository;

import com.mercadolibro.entity.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRoleRepository extends JpaRepository<AppUserRole, Integer> {
    Optional<AppUserRole> findByDescription(String description);
}
