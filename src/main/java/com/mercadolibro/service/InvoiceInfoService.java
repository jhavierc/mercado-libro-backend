package com.mercadolibro.service;

import com.mercadolibro.dto.InvoiceInfoDTO;
import com.mercadolibro.entities.InvoiceInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvoiceInfoService {

     InvoiceInfoDTO findById(Long id);
     List<InvoiceInfoDTO> findAll();
     InvoiceInfoDTO save(InvoiceInfo invoiceInfo);

}
