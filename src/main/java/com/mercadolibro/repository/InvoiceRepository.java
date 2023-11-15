package com.mercadolibro.repository;

import com.mercadolibro.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query(value = "SELECT id FROM invoice ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Long findLastInsertedId();

    Page<Invoice> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM invoice WHERE user_id = ?1",
            countQuery = "SELECT count(*) FROM invoice WHERE user_id = ?1",
            nativeQuery = true)
    Page<Invoice> findByUserId(Long id, Pageable pageable);

}
