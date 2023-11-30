package com.mercadolibro.repository;

import com.mercadolibro.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Integer> {

    Optional<Address> findByUserId(Integer userId);

    @Query("SELECT a FROM Address a JOIN AppUser u ON u.id = a.userId WHERE u.email = ?1")
    Optional<Address> findByUserEmail(String email);
}
