package com.mercadolibro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.InvoiceDTO;
import com.mercadolibro.entities.Invoice;
import com.mercadolibro.repository.InvoiceRepository;
import com.mercadolibro.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    ObjectMapper mapper;

    @Override
    public InvoiceDTO findById(Long id) {
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
        InvoiceDTO invoiceDTO = null;
        if (optionalInvoice.isPresent()) {
            invoiceDTO = mapper.convertValue(optionalInvoice, InvoiceDTO.class);
        }
        return invoiceDTO;
    }

    @Override
    public List<InvoiceDTO> findAll() {
        List<Invoice> invoiceList = invoiceRepository.findAll();
        List<InvoiceDTO> invoiceDTOList = null;
        for (Invoice invoice: invoiceList) {
            invoiceDTOList.add(mapper.convertValue(invoice, InvoiceDTO.class));
        }
        return invoiceDTOList;
    }

    @Override
    public InvoiceDTO save(Invoice invoice) {
        Invoice createdInvoice = invoiceRepository.save(invoice);
        InvoiceDTO createdInvoiceDTO = mapper.convertValue(createdInvoice, InvoiceDTO.class);
        return createdInvoiceDTO;
    }
}
