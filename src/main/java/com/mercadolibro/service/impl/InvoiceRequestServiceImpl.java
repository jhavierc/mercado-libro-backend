package com.mercadolibro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.*;
import com.mercadolibro.entity.Invoice;
import com.mercadolibro.entity.InvoiceRequest;
import com.mercadolibro.entity.InvoiceItem;
import com.mercadolibro.repository.InvoiceRepository;
import com.mercadolibro.repository.InvoiceItemRepository;
import com.mercadolibro.service.InvoiceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceRequestServiceImpl implements InvoiceRequestService {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    InvoiceItemRepository invoiceItemRepository;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    BookServiceImpl bookService;

    @Override
    public InvoiceRequestDTO save(InvoiceRequest invoiceRequest) {
        // Invoice
        Invoice createdInvoice = invoiceRepository.save(invoiceRequest.getInvoice());
        InvoiceDTO createdInvoiceDTO = mapper.convertValue(createdInvoice, InvoiceDTO.class);
        // InvoiceItem
        List<InvoiceItemDTO> createdInvoiceItemDTOList = new ArrayList<>();
        for (InvoiceItem invoiceItem: invoiceRequest.getInvoiceItemList()) {
            invoiceItem.setInvoiceId(createdInvoiceDTO.getId());
            InvoiceItem createdInvoiceItem = invoiceItemRepository.save(invoiceItem);
            createdInvoiceItemDTOList.add(mapper.convertValue(createdInvoiceItem, InvoiceItemDTO.class));
        }
        // InvoiceRequest
        return new InvoiceRequestDTO(createdInvoiceDTO, createdInvoiceItemDTOList);
    }

    @Override
    public InvoiceRequestDTO findById(Long id) {
        // Invoice
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
        InvoiceDTO invoiceDTO = null;
        if (optionalInvoice.isPresent()) {
            invoiceDTO = mapper.convertValue(optionalInvoice, InvoiceDTO.class);
        }
        // InvoiceItem
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findByInvoiceId(id);
        List<InvoiceItemDTO> invoiceItemDTOList = new ArrayList<>();;
        for (InvoiceItem invoiceItem: invoiceItemList) {
            invoiceItemDTOList.add(mapper.convertValue(invoiceItem, InvoiceItemDTO.class));
        }
        // InvoiceRequest
        return new InvoiceRequestDTO(invoiceDTO, invoiceItemDTOList);
    }

    public PageDTO<InvoiceRequestDTO> findAll(int page, int size) {
        Page<Invoice> invoicePage = invoiceRepository.findAll(PageRequest.of(page-1,size));
        return invoicePage_to_PageDTO(invoicePage);
    }

    public PageDTO<InvoiceRequestDTO> findByUserId(Long userId, int page, int size) {
        Page<Invoice> invoicePage = invoiceRepository.findByUserId(userId, PageRequest.of(page-1,size));
        return invoicePage_to_PageDTO(invoicePage);
    }

    private PageDTO<InvoiceRequestDTO> invoicePage_to_PageDTO(Page<Invoice> invoicePage) {
        // Invoice
        List<Invoice> invoiceList = invoicePage.getContent();
        List<InvoiceDTO> invoiceDTOList = new ArrayList<>();
        for (Invoice invoice : invoiceList) {
            invoiceDTOList.add(mapper.convertValue(invoice, InvoiceDTO.class));
        }
        // InvoiceRequest
        List<InvoiceRequestDTO> invoiceRequestDTOList = new ArrayList<>();
        for (InvoiceDTO invoiceDTO : invoiceDTOList) {
            // InvoiceItem
            List<InvoiceItem> invoiceItemList = invoiceItemRepository.findByInvoiceId(invoiceDTO.getId());
            List<InvoiceItemDTO> invoiceItemDTOList = new ArrayList<>();
            for (InvoiceItem invoiceItem: invoiceItemList) {
                invoiceItemDTOList.add(mapper.convertValue(invoiceItem, InvoiceItemDTO.class));
            }
            invoiceRequestDTOList.add(new InvoiceRequestDTO(invoiceDTO, invoiceItemDTOList));
        }
        // PageDTO
        return new PageDTO<>(
                invoiceRequestDTOList,
                invoicePage.getTotalPages(),
                invoicePage.getTotalElements(),
                invoicePage.getNumber(),
                invoicePage.getSize()
        );
    }

    public List<BookRespDTO> findBestSellers() {
        List<InvoiceItem> invoiceItemPage = invoiceItemRepository.findBestSellers();
        List<Long> longList = new ArrayList<>();
        for (InvoiceItem invoiceItem: invoiceItemPage) {
            longList.add(invoiceItem.getBookId());
        }
        List<BookRespDTO> bookRespDTOPageDTO = new ArrayList<>();
        for (Long bookId: longList) {
            try {
                bookRespDTOPageDTO.add(bookService.findByID(bookId));
            }
            catch(Exception ignored) {
            }
        }
        return bookRespDTOPageDTO;
    }

    public PageDTO<MonthlySaleDTO> getMonthlySales(int page, int size) {
        Page<MonthlySaleDTO> invoicePage = invoiceRepository.getMonthlySales(PageRequest.of(page-1,size));
        return new PageDTO<>(
                invoicePage.getContent(),
                invoicePage.getTotalPages(),
                invoicePage.getTotalElements(),
                invoicePage.getNumber(),
                invoicePage.getSize()
        );
    }

    public PageDTO<CategorySalesDTO> getSalesByCategory(int page, int size) {
        Page<CategorySalesDTO> invoicePage = invoiceRepository.getSalesByCategory(PageRequest.of(page-1,size));
        return new PageDTO<>(
                invoicePage.getContent(),
                invoicePage.getTotalPages(),
                invoicePage.getTotalElements(),
                invoicePage.getNumber(),
                invoicePage.getSize()
        );
    }



}
