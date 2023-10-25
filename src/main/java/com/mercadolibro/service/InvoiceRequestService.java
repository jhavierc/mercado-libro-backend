package com.mercadolibro.service;

import com.mercadolibro.dto.InvoiceRequestDTO;
import com.mercadolibro.entities.InvoiceRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvoiceRequestService {

    InvoiceRequestDTO findById(Long id);
    List<InvoiceRequestDTO> findAll();
    InvoiceRequestDTO save(InvoiceRequest invoiceRequest);

}
