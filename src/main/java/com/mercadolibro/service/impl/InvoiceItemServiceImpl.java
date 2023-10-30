package com.mercadolibro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.InvoiceItemDTO;
import com.mercadolibro.entity.InvoiceItem;
import com.mercadolibro.repository.InvoiceItemRepository;
import com.mercadolibro.service.InvoiceItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceItemServiceImpl implements InvoiceItemService {

    @Autowired
    InvoiceItemRepository invoiceItemRepository;

    @Autowired
    ObjectMapper mapper;


    @Override
    public InvoiceItemDTO findById(Long id) {
        Optional<InvoiceItem> optionalInvoiceItem = invoiceItemRepository.findById(id);
        InvoiceItemDTO invoiceItemDTO = null;
        if (optionalInvoiceItem.isPresent()) {
            invoiceItemDTO = mapper.convertValue(optionalInvoiceItem, InvoiceItemDTO.class);
        }
        return invoiceItemDTO;
    }

    @Override
    public List<InvoiceItemDTO> findAll() {
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        List<InvoiceItemDTO> invoiceItemDTOList = null;
        for (InvoiceItem invoiceItem: invoiceItemList) {
            invoiceItemDTOList.add(mapper.convertValue(invoiceItem, InvoiceItemDTO.class));
        }
        return invoiceItemDTOList;
    }

    @Override
    public InvoiceItemDTO save(InvoiceItem invoice) {
        InvoiceItem createdInvoiceItem = invoiceItemRepository.save(invoice);
        InvoiceItemDTO createdInvoiceItemDTO = mapper.convertValue(createdInvoiceItem, InvoiceItemDTO.class);
        return createdInvoiceItemDTO;
    }
}
