package com.mercadolibro.repository;

import com.mercadolibro.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query(value = "SELECT id FROM invoice ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Long findLastInsertedId();

    Page<Invoice> findAll(Pageable pageable);

    @Query(value = "SELECT address from invoice JOIN user ON invoice.user_id = user.id WHERE user.email = ?1", nativeQuery = true)
    List<String> findInvoicesAddressByUserEmail(String email);

}
