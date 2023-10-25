package com.mercadolibro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.InvoiceDTO;
import com.mercadolibro.entities.Invoice;
import com.mercadolibro.repository.InvoiceInfoRepository;
import com.mercadolibro.service.InvoiceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceInfoServiceImpl implements InvoiceInfoService {

    @Autowired
    InvoiceInfoRepository invoiceInfoRepository;

    @Autowired
    ObjectMapper mapper;

    @Override
    public InvoiceDTO findById(Long id) {
        Optional<Invoice> optionalInvoice = invoiceInfoRepository.findById(id);
        InvoiceDTO invoiceDTO = null;
        if (optionalInvoice.isPresent()) {
            invoiceDTO = mapper.convertValue(optionalInvoice, InvoiceDTO.class);
        }
        return invoiceDTO;
    }

    @Override
    public List<InvoiceDTO> findAll() {
        List<Invoice> invoiceList = invoiceInfoRepository.findAll();
        List<InvoiceDTO> invoiceDTOList = null;
        for (Invoice invoice : invoiceList) {
            invoiceDTOList.add(mapper.convertValue(invoice, InvoiceDTO.class));
        }
        return invoiceDTOList;
    }

    @Override
    public InvoiceDTO save(Invoice invoice) {
        Invoice createdInvoice = invoiceInfoRepository.save(invoice);
        InvoiceDTO createdInvoiceDTO = mapper.convertValue(createdInvoice, InvoiceDTO.class);
        return createdInvoiceDTO;
    }
}
