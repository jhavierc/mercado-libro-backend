package com.mercadolibro.service;

import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.dto.InvoiceRequestDTO;
import com.mercadolibro.dto.PageDTO;
import com.mercadolibro.entity.Book;
import com.mercadolibro.entity.InvoiceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvoiceRequestService {

    InvoiceRequestDTO findById(Long id);
    InvoiceRequestDTO save(InvoiceRequest invoiceRequest);
    PageDTO<InvoiceRequestDTO> findAll(int page, int size);
    PageDTO<InvoiceRequestDTO> findByUserId(Long userId, int page, int size);
    List<BookRespDTO> findBestSellers();

}
