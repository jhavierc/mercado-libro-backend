package com.mercadolibro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.InvoiceInfoDTO;
import com.mercadolibro.entities.InvoiceInfo;
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
    public InvoiceInfoDTO findById(Long id) {
        Optional<InvoiceInfo> optionalInvoice = invoiceInfoRepository.findById(id);
        InvoiceInfoDTO invoiceInfoDTO = null;
        if (optionalInvoice.isPresent()) {
            invoiceInfoDTO = mapper.convertValue(optionalInvoice, InvoiceInfoDTO.class);
        }
        return invoiceInfoDTO;
    }

    @Override
    public List<InvoiceInfoDTO> findAll() {
        List<InvoiceInfo> invoiceInfoList = invoiceInfoRepository.findAll();
        List<InvoiceInfoDTO> invoiceInfoDTOList = null;
        for (InvoiceInfo invoiceInfo : invoiceInfoList) {
            invoiceInfoDTOList.add(mapper.convertValue(invoiceInfo, InvoiceInfoDTO.class));
        }
        return invoiceInfoDTOList;
    }

    @Override
    public InvoiceInfoDTO save(InvoiceInfo invoiceInfo) {
        InvoiceInfo createdInvoiceInfo = invoiceInfoRepository.save(invoiceInfo);
        InvoiceInfoDTO createdInvoiceInfoDTO = mapper.convertValue(createdInvoiceInfo, InvoiceInfoDTO.class);
        return createdInvoiceInfoDTO;
    }
}
