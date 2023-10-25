package com.mercadolibro.repository;

import com.mercadolibro.entities.InvoiceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceInfoRepository extends JpaRepository<InvoiceInfo, Long> {



}
