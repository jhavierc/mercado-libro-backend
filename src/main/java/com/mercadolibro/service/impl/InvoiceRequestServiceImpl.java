package com.mercadolibro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.*;
import com.mercadolibro.entity.Invoice;
import com.mercadolibro.entity.InvoiceRequest;
import com.mercadolibro.entity.InvoiceItem;
import com.mercadolibro.exception.InvoicePaymentException;
import com.mercadolibro.repository.InvoiceRepository;
import com.mercadolibro.repository.InvoiceItemRepository;
import com.mercadolibro.service.InvoiceRequestService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InvoiceRequestServiceImpl implements InvoiceRequestService {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    InvoiceItemRepository invoiceItemRepository;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    String mercadoPagoAccessToken;

    @Override
    public InvoiceRequestDTO findById(UUID id) {
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
    public PageDTO<InvoiceRequestDTO> findAll(int page, int size) {
        // Invoice
        Page<Invoice> invoicePage = invoiceRepository.findAll(PageRequest.of(page-1,size));
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

    @Override
    public PaymentRespDTO processPayment(UUID invoiceId, PaymentReqDTO paymentReqDTO) {
        try {
            MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);

            Map<String, String> customHeaders = new HashMap<>();
            customHeaders.put("x-idempotency-key", invoiceId.toString());

            MPRequestOptions requestOptions = MPRequestOptions.builder()
                    .customHeaders(customHeaders)
                    .build();

            PaymentClient client = new PaymentClient();

            PaymentCreateRequest paymentCreateRequest =
                    PaymentCreateRequest.builder()
                            .transactionAmount(paymentReqDTO.getTransactionAmount())
                            .token(paymentReqDTO.getToken())
                            .description(paymentReqDTO.getDescription())
                            .installments(paymentReqDTO.getInstallments())
                            .paymentMethodId(paymentReqDTO.getPaymentMethodId())
                            .payer(
                                    PaymentPayerRequest.builder()
                                            .email(paymentReqDTO.getPayer().getEmail())
                                            .firstName(paymentReqDTO.getPayer().getFirstName())
                                            .identification(
                                                    IdentificationRequest.builder()
                                                            .type(paymentReqDTO.getPayer().getIdentification().getType())
                                                            .number(paymentReqDTO.getPayer().getIdentification().getNumber())
                                                            .build())
                                            .build())
                            .build();

            Payment payment = client.create(paymentCreateRequest, requestOptions);
            return mapper.convertValue(payment, PaymentRespDTO.class);
        } catch (MPException | MPApiException e) {
            throw new InvoicePaymentException(e.getMessage());
        }
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
