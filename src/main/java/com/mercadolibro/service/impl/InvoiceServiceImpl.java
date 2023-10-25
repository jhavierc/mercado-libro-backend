package com.mercadolibro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.InvoiceDTO;
import com.mercadolibro.dto.InvoiceInfoDTO;
import com.mercadolibro.dto.InvoiceItemDTO;
import com.mercadolibro.entities.Invoice;
import com.mercadolibro.entities.InvoiceInfo;
import com.mercadolibro.entities.InvoiceItem;
import com.mercadolibro.repository.InvoiceInfoRepository;
import com.mercadolibro.repository.InvoiceItemRepository;
import com.mercadolibro.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public InvoiceDTO findById(Long id) {
        // InvoiceInfo
        Optional<InvoiceInfo> optionalInvoiceInfo = invoiceInfoRepository.findById(id);
        InvoiceInfoDTO invoiceInfoDTO = null;
        if (optionalInvoiceInfo.isPresent()) {
            invoiceInfoDTO = mapper.convertValue(optionalInvoiceInfo, InvoiceInfoDTO.class);
        }
        // InvoiceItem
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findByInvoiceInfoId(id);
        List<InvoiceItemDTO> invoiceItemDTOList = null;
        for (InvoiceItem invoiceItem: invoiceItemList) {
            invoiceItemDTOList.add(mapper.convertValue(invoiceItem, InvoiceItemDTO.class));
        }
        // Invoice
        return new InvoiceDTO(invoiceInfoDTO, invoiceItemDTOList);
    }

    @Override
    public List<InvoiceDTO> findAll() {
        // InvoiceInfo
        List<InvoiceInfo> invoiceInfoList = invoiceInfoRepository.findAll();
        List<InvoiceInfoDTO> invoiceInfoDTOList = null;
        for (InvoiceInfo invoiceInfo : invoiceInfoList) {
            invoiceInfoDTOList.add(mapper.convertValue(invoiceInfo, InvoiceInfoDTO.class));
        }
        // Invoice
        List<InvoiceDTO> invoiceDTOList = null;
        for (InvoiceInfoDTO invoiceInfoDTO: invoiceInfoDTOList) {
            // InvoiceItem
            List<InvoiceItem> invoiceItemList = invoiceItemRepository.findByInvoiceInfoId(invoiceInfoDTO.getId());
            List<InvoiceItemDTO> invoiceItemDTOList = null;
            for (InvoiceItem invoiceItem: invoiceItemList) {
                invoiceItemDTOList.add(mapper.convertValue(invoiceItem, InvoiceItemDTO.class));
            }
            invoiceDTOList.add(new InvoiceDTO(invoiceInfoDTO, invoiceItemDTOList));
        }
        return invoiceDTOList;
    }

    @Override
    public InvoiceDTO save(Invoice invoice) {
        // InvoiceInfo
        InvoiceInfo createdInvoiceInfo = invoiceInfoRepository.save(invoice.getInvoiceInfo());
        InvoiceInfoDTO createdInvoiceInfoDTO = mapper.convertValue(createdInvoiceInfo, InvoiceInfoDTO.class);
        // InvoiceItem
        List<InvoiceItemDTO> createdInvoiceItemDTOList = null;
        for (InvoiceItem invoiceItem: invoice.getInvoiceItemList()) {
            InvoiceItem createdInvoiceItem = invoiceItemRepository.save(invoiceItem);
            createdInvoiceItemDTOList.add(mapper.convertValue(createdInvoiceItem, InvoiceItemDTO.class));
        }
        // Invoice
        return new InvoiceDTO(createdInvoiceInfoDTO, createdInvoiceItemDTOList);
    }


}
