package com.mercadolibro.repository;

import com.mercadolibro.dto.CategorySalesDTO;
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
            countQuery = "SELECT count(*) FROM invoice GROUP BY MONTH(date_created)",
            nativeQuery = true)
    Page<MonthlySaleDTO> getMonthlySales(Pageable pageable);

    @Query(value = "SELECT category.name as category_name, count(*) as sales" +
            "            FROM invoice" +
            "            INNER JOIN invoice_item ON invoice_item.invoice_id = invoice.id" +
            "            INNER JOIN book ON book.id = invoice_item.book_id" +
            "            INNER JOIN book_categories ON book_categories.book_id = book.id" +
            "            INNER JOIN category ON category.id = book_categories.category_id" +
            "            GROUP BY category.name" +
            "            ORDER BY count(*) desc",
            nativeQuery = true)
    Page<CategorySalesDTO> getSalesByCategory(Pageable pageable);


}
