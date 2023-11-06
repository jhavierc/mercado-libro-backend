package com.mercadolibro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.InvoiceRequestDTO;
import com.mercadolibro.dto.InvoiceDTO;
import com.mercadolibro.dto.InvoiceItemDTO;
import com.mercadolibro.entity.Invoice;
import com.mercadolibro.entity.InvoiceRequest;
import com.mercadolibro.entity.InvoiceItem;
import com.mercadolibro.repository.InvoiceRepository;
import com.mercadolibro.repository.InvoiceItemRepository;
import com.mercadolibro.service.InvoiceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public InvoiceRequestDTO findById(Long id) {
        // Invoice
        Optional<Invoice> optionalInvoiceInfo = invoiceRepository.findById(id);
        InvoiceDTO invoiceDTO = null;
        if (optionalInvoiceInfo.isPresent()) {
            invoiceDTO = mapper.convertValue(optionalInvoiceInfo, InvoiceDTO.class);
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

    @Override
    public List<InvoiceRequestDTO> findAll() {
        // Invoice
        List<Invoice> invoiceList = invoiceRepository.findAll();
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
        return invoiceRequestDTOList;
    }

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


}
