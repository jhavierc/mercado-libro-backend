package com.mercadolibro.service;

import com.mercadolibro.dto.InvoiceDTO;
import com.mercadolibro.entity.Invoice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvoiceService {

     InvoiceDTO findById(Long id);
     List<InvoiceDTO> findAll();
     InvoiceDTO save(Invoice invoice);

}
