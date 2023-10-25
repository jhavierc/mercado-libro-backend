package com.mercadolibro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.InvoiceRequestDTO;
import com.mercadolibro.dto.InvoiceDTO;
import com.mercadolibro.dto.InvoiceItemDTO;
import com.mercadolibro.entities.InvoiceRequest;
import com.mercadolibro.entities.Invoice;
import com.mercadolibro.entities.InvoiceItem;
import com.mercadolibro.repository.InvoiceInfoRepository;
import com.mercadolibro.repository.InvoiceItemRepository;
import com.mercadolibro.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    InvoiceInfoRepository invoiceInfoRepository;

    @Autowired
    InvoiceItemRepository invoiceItemRepository;

    @Autowired
    ObjectMapper mapper;

    @Override
    public InvoiceRequestDTO findById(Long id) {
        // InvoiceInfo
        Optional<Invoice> optionalInvoiceInfo = invoiceInfoRepository.findById(id);
        InvoiceDTO invoiceDTO = null;
        if (optionalInvoiceInfo.isPresent()) {
            invoiceDTO = mapper.convertValue(optionalInvoiceInfo, InvoiceDTO.class);
        }
        // InvoiceItem
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findByInvoiceId(id);
        List<InvoiceItemDTO> invoiceItemDTOList = null;
        for (InvoiceItem invoiceItem: invoiceItemList) {
            invoiceItemDTOList.add(mapper.convertValue(invoiceItem, InvoiceItemDTO.class));
        }
        // Invoice
        return new InvoiceRequestDTO(invoiceDTO, invoiceItemDTOList);
    }

    @Override
    public List<InvoiceRequestDTO> findAll() {
        // InvoiceInfo
        List<Invoice> invoiceList = invoiceInfoRepository.findAll();
        List<InvoiceDTO> invoiceDTOList = new ArrayList<>();
        for (Invoice invoice : invoiceList) {
            invoiceDTOList.add(mapper.convertValue(invoice, InvoiceDTO.class));
        }
        // Invoice
        List<InvoiceRequestDTO> invoiceRequestDTOList = new ArrayList<>();;
        for (InvoiceDTO invoiceDTO : invoiceDTOList) {
            // InvoiceItem
            List<InvoiceItem> invoiceItemList = invoiceItemRepository.findByInvoiceId(invoiceDTO.getId());
            List<InvoiceItemDTO> invoiceItemDTOList = new ArrayList<>();;
            for (InvoiceItem invoiceItem: invoiceItemList) {
                invoiceItemDTOList.add(mapper.convertValue(invoiceItem, InvoiceItemDTO.class));
            }
            invoiceRequestDTOList.add(new InvoiceRequestDTO(invoiceDTO, invoiceItemDTOList));
        }
        return invoiceRequestDTOList;
    }

    @Override
    public InvoiceRequestDTO save(InvoiceRequest invoiceRequest) {
        // InvoiceInfo
        Invoice createdInvoice = invoiceInfoRepository.save(invoiceRequest.getInvoice());
        InvoiceDTO createdInvoiceDTO = mapper.convertValue(createdInvoice, InvoiceDTO.class);
        // InvoiceItem
        List<InvoiceItemDTO> createdInvoiceItemDTOList = new ArrayList<>();;
        for (InvoiceItem invoiceItem: invoiceRequest.getInvoiceItemList()) {
            InvoiceItem createdInvoiceItem = invoiceItemRepository.save(invoiceItem);
            createdInvoiceItemDTOList.add(mapper.convertValue(createdInvoiceItem, InvoiceItemDTO.class));
        }
        // Invoice
        return new InvoiceRequestDTO(createdInvoiceDTO, createdInvoiceItemDTOList);
    }


}
