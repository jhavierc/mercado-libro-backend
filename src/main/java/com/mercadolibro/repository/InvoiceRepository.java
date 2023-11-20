package com.mercadolibro.repository;

import com.mercadolibro.dto.MonthlySaleDTO;
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

    @Query(value = "SELECT YEAR(date_created) as year,MONTH(date_created) as month,count(*) as sales " +
                "FROM invoice " +
                "WHERE date_created IS NOT NULL " +
                "GROUP BY MONTH(date_created) " +
                "ORDER BY year DESC, month DESC",
            countQuery = "SELECT count(*) FROM invoice GROUP BY date_created",
            nativeQuery = true)
    Page<MonthlySaleDTO> getMonthlySales(Pageable pageable);

}
