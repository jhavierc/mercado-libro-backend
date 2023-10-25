package com.mercadolibro.repository;

import com.mercadolibro.entities.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {

    //@Query("FROM invoice_item WHERE invoice_id = :invoiceId")
    List<InvoiceItem> findByInvoiceInfoId(/*@Param("invoiceId")*/ Long invoiceId);

}
