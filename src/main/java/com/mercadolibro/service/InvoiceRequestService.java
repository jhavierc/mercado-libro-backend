package com.mercadolibro.service;

import com.mercadolibro.dto.InvoiceRequestDTO;
import com.mercadolibro.dto.InvoiceSearchDTO;
import com.mercadolibro.dto.PageDTO;
import com.mercadolibro.entity.InvoiceRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvoiceRequestService {

    InvoiceRequestDTO findById(Long id);
    List<InvoiceSearchDTO> findAll();
    InvoiceRequestDTO save(InvoiceRequest invoiceRequest);
    PageDTO<InvoiceSearchDTO> findAll(int page, int size);
}
