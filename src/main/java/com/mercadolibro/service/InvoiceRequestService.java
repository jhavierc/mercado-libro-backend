package com.mercadolibro.service;

import com.mercadolibro.dto.*;
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
    PageDTO<CategorySalesDTO> getSalesByCategory(int page, int size);
}
