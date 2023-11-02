package com.mercadolibro.repository;

import com.mercadolibro.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    boolean existsByEmail(String email);
}
