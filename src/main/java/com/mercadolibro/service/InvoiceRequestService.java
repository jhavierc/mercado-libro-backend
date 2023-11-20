package com.mercadolibro.service;

import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.dto.InvoiceRequestDTO;
import com.mercadolibro.dto.MonthlySaleDTO;
import com.mercadolibro.dto.PageDTO;
import com.mercadolibro.entity.InvoiceRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvoiceRequestService {

    InvoiceRequestDTO findById(Long id);
    InvoiceRequestDTO save(InvoiceRequest invoiceRequest);
    PageDTO<InvoiceRequestDTO> findAll(int page, int size);
    PageDTO<InvoiceRequestDTO> findByUserId(Long userId, int page, int size);
    List<BookRespDTO> findBestSellers();
    PageDTO<MonthlySaleDTO> getMonthlySales(int page, int size);
}
