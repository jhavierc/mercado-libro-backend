package com.mercadolibro.service;

import com.mercadolibro.dto.InvoiceRequestDTO;
import com.mercadolibro.entity.InvoiceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvoiceRequestService {

    InvoiceRequestDTO findById(Long id);
    List<InvoiceRequestDTO> findAll();
    InvoiceRequestDTO save(InvoiceRequest invoiceRequest);

    List<InvoiceRequestDTO> findAll(int page, int size, Boolean sortAsc, String column);
}
