package com.mercadolibro.service;

import com.mercadolibro.dto.InvoiceItemDTO;
import com.mercadolibro.entities.InvoiceItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvoiceItemService {

    InvoiceItemDTO findById(Long id);
    List<InvoiceItemDTO> findAll();
    InvoiceItemDTO save(InvoiceItem invoiceItem);

}
