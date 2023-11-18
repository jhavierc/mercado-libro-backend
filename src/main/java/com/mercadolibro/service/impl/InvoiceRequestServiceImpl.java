package com.mercadolibro.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.*;
import com.mercadolibro.entity.Book;
import com.mercadolibro.entity.Invoice;
import com.mercadolibro.entity.InvoiceRequest;
import com.mercadolibro.entity.InvoiceItem;
import com.mercadolibro.exception.InvoicePaymentException;
import com.mercadolibro.repository.BookRepository;
import com.mercadolibro.repository.InvoiceRepository;
import com.mercadolibro.repository.InvoiceItemRepository;
import com.mercadolibro.service.BookService;
import com.mercadolibro.service.InvoiceRequestService;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.*;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.net.MPResponse;
import com.mercadopago.resources.payment.Payment;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class InvoiceRequestServiceImpl implements InvoiceRequestService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final ObjectMapper mapper;
    private final String mercadoPagoAccessToken;
    private final BookRepository bookRepository;
    private final BookService bookService;


    public static final String INSUFFICIENT_STOCK_FOR_BOOK = "Insufficient stock for book '%s'";
    public static final String INVOICE_ALREADY_PAID = "Invoice with ID '%s' is already paid";
    public static final String INVOICE_NOT_FOUND = "Invoice with ID '%s' not found";


    public InvoiceRequestServiceImpl(InvoiceRepository invoiceRepository, InvoiceItemRepository invoiceItemRepository, ObjectMapper mapper, String mercadoPagoAccessToken, BookRepository bookRepository, BookService bookService) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceItemRepository = invoiceItemRepository;
        this.mapper = mapper;
        this.mercadoPagoAccessToken = mercadoPagoAccessToken;
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    @Override
    public InvoiceRequestDTO findById(UUID id) {
        // Invoice
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
        if (optionalInvoice.isEmpty()) {
            throw new InvoicePaymentException(String.format(INVOICE_NOT_FOUND, id), HttpStatus.NOT_FOUND.value());
        }

        InvoiceDTO invoiceDTO = mapper.convertValue(optionalInvoice, InvoiceDTO.class);

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
    @Transactional
    public PaymentRespDTO processPayment(UUID invoiceId, PaymentReqDTO paymentReqDTO) {
        checkBookStocks(invoiceId);

        try {
            Map<String, String> customHeaders = new HashMap<>();
            customHeaders.put("x-idempotency-key", invoiceId.toString());

            MPRequestOptions requestOptions = MPRequestOptions.builder()
                    .customHeaders(customHeaders)
                    .accessToken(mercadoPagoAccessToken)
                    .build();

            PaymentClient client = new PaymentClient();

            PaymentCreateRequest paymentCreateRequest =
                    PaymentCreateRequest.builder()
                            .transactionAmount(paymentReqDTO.getTransactionAmount())
                            .token(paymentReqDTO.getToken())
                            .description(paymentReqDTO.getDescription())
                            .installments(paymentReqDTO.getInstallments())
                            .payer(
                                    PaymentPayerRequest.builder()
                                            .email(paymentReqDTO.getPayer().getEmail())
                                            .identification(
                                                    IdentificationRequest.builder()
                                                            .type(paymentReqDTO.getPayer().getIdentification().getType())
                                                            .number(paymentReqDTO.getPayer().getIdentification().getNumber())
                                                            .build())
                                            .build())
                            .build();

            PaymentRespDTO paymentResponse;

            Payment payment = client.create(paymentCreateRequest, requestOptions);
            if (payment.getStatus().equals("approved") || payment.getStatus().equals("in_process")) {
                updateBookStocksAndInvoiceStatus(invoiceId);
                paymentResponse = mapper.convertValue(payment, PaymentRespDTO.class);
            } else {
                PaymentStatus paymentStatus = PaymentStatus.getById(payment.getCard().getCardholder().getName());
                throw new InvoicePaymentException(paymentStatus.getStatus(), paymentStatus.getDetails(), HttpStatus.CONFLICT.value());
            }

            return paymentResponse;
        } catch (MPException e) {
            throw new InvoicePaymentException(e.getMessage());
        } catch (MPApiException e) {
            MPResponse response = e.getApiResponse();
            try {
                MercadoPagoErrorResponse mercadoPagoError = mapper.readValue(response.getContent(), MercadoPagoErrorResponse.class);
                throw new InvoicePaymentException(mercadoPagoError.getMessage(), mercadoPagoError.getStatus());
            } catch (JsonProcessingException jsonException) {
                throw new InvoicePaymentException(jsonException.getMessage());
            }
        }
    }


    @Override
    public InvoiceRequestDTO save(InvoiceRequest invoiceRequest) {
        // Invoice
        Invoice createdInvoice = invoiceRepository.save(invoiceRequest.getInvoice());
        createdInvoice.setPaid(false);
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

    private void checkBookStocks(UUID invoiceId) {
        InvoiceRequestDTO invoiceRequestDTO = findById(invoiceId);
        if (invoiceRequestDTO.getInvoiceDTO().getPaid()) {
            throw new InvoicePaymentException(String.format(INVOICE_ALREADY_PAID, invoiceId), HttpStatus.BAD_REQUEST.value());
        }

        List<String> insufficientStockBooks = new ArrayList<>();
        for (InvoiceItemDTO invoiceItem : invoiceRequestDTO.getInvoiceItemDTOList()) {
            BookRespDTO book = bookService.findByID(invoiceItem.getBookId());
            if (book.getStock() < invoiceItem.getQuantity()) {
                insufficientStockBooks.add(book.getTitle());
            }
        }

        if (!insufficientStockBooks.isEmpty()) {
            throw new InvoicePaymentException(String.format(INSUFFICIENT_STOCK_FOR_BOOK, String.join(", ", insufficientStockBooks)), HttpStatus.CONFLICT.value());
        }
    }

    private void updateBookStocksAndInvoiceStatus(UUID invoiceId) {
        InvoiceRequestDTO invoiceRequestDTO = findById(invoiceId);

        List<Book> booksToSave = new ArrayList<>();
        for (InvoiceItemDTO invoiceItem : invoiceRequestDTO.getInvoiceItemDTOList()) {
            BookRespDTO book = bookService.findByID(invoiceItem.getBookId());
            book.setStock(book.getStock() - invoiceItem.getQuantity());
            booksToSave.add(mapper.convertValue(book, Book.class));
        }

        bookRepository.saveAll(booksToSave);

        Invoice invoice = mapper.convertValue(invoiceRequestDTO.getInvoiceDTO(), Invoice.class);
        invoice.setPaid(true);
        invoiceRepository.save(invoice);
    }
}
