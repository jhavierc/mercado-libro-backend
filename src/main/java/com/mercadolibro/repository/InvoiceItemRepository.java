package com.mercadolibro.repository;

import com.mercadolibro.entity.InvoiceItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {

    List<InvoiceItem> findByInvoiceId(Long invoiceId);

    @Query(value = "SELECT id FROM invoice_item ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Long findLastInsertedId();

    Page<InvoiceItem> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM invoice_item GROUP BY book_id ORDER BY COUNT(*) DESC",
            countQuery = "SELECT count(*) FROM invoice_item GROUP BY book_id ORDER BY COUNT(*) DESC",
            nativeQuery = true)
    List<InvoiceItem> findBestSellers();

}
