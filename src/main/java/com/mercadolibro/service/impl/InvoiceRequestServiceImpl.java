package com.mercadolibro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibro.dto.*;
import com.mercadolibro.entity.Invoice;
import com.mercadolibro.entity.InvoiceRequest;
import com.mercadolibro.entity.InvoiceItem;
import com.mercadolibro.repository.BookRepository;
import com.mercadolibro.repository.InvoiceRepository;
import com.mercadolibro.repository.InvoiceItemRepository;
import com.mercadolibro.service.InvoiceRequestService;
import com.mercadolibro.service.UserService;
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
    BookRepository bookRepository;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    BookServiceImpl bookService;

    @Autowired
    UserService userService;

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
    public InvoiceSearchDTO findById(Long id) {
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
        InvoiceSearchDTO invoiceSearchDTO = null;
        if (optionalInvoice.isPresent()) {
            invoiceSearchDTO = mapper.convertValue(optionalInvoice, InvoiceSearchDTO.class);
            invoiceSearchDTO.setListInvoiceItems(getListItems(invoiceSearchDTO.getId()));
        }
        return invoiceSearchDTO;
    }


    @Override
    public List<InvoiceSearchDTO> findAll() {
        // Invoice
        List<Invoice> invoiceList = invoiceRepository.findAll();
        List<InvoiceSearchDTO> invoiceDTOList = new ArrayList<>();
        for (Invoice invoice : invoiceList) {
            invoiceDTOList.add(mapper.convertValue(invoice, InvoiceSearchDTO.class));
        }

        for (InvoiceSearchDTO invoiceDTO : invoiceDTOList) {
            invoiceDTO.setListInvoiceItems(getListItems(invoiceDTO.getId()));
        }
        return invoiceDTOList;
    }

    @Override
    public PageDTO<InvoiceSearchDTO> findAll(int page, int size) {
        // Invoice
        Page<Invoice> invoicePage = invoiceRepository.findAll(PageRequest.of(page-1,size));
        List<Invoice> invoiceList = invoicePage.getContent();
        List<InvoiceSearchDTO> invoiceDTOList = new ArrayList<>();
        for (Invoice invoice : invoiceList) {
            invoiceDTOList.add(mapper.convertValue(invoice, InvoiceSearchDTO.class));
        }
        for (InvoiceSearchDTO invoiceDTO : invoiceDTOList) {
            invoiceDTO.setListInvoiceItems(getListItems(invoiceDTO.getId()));
        }
        //TODO: Ver si incluir o no la info de cada User, porque la consulta de invoices dura mas de 40 segundos.
        /*for (InvoiceSearchDTO invoiceDTO : invoiceDTOList) {
            invoiceDTO.setClientDTO(getUser(invoiceDTO.getId()));
        }*/
        // PageDTO
        return new PageDTO<>(
                invoiceDTOList,
                invoicePage.getTotalPages(),
                invoicePage.getTotalElements(),
                invoicePage.getNumber(),
                invoicePage.getSize()
        );
    }

    @Override
    public PageDTO<InvoiceSearchDTO> findByUserId(Long userId, int page, int size) {
        Page<Invoice> invoicePage = invoiceRepository.findByUserId(userId, PageRequest.of(page-1,size));
        List<Invoice> invoiceList = invoicePage.getContent();
        List<InvoiceSearchDTO> invoiceDTOList = new ArrayList<>();
        for (Invoice invoice : invoiceList) {
            invoiceDTOList.add(mapper.convertValue(invoice, InvoiceSearchDTO.class));
        }
        for (InvoiceSearchDTO invoiceDTO : invoiceDTOList) {
            invoiceDTO.setListInvoiceItems(getListItems(invoiceDTO.getId()));
        }
        return new PageDTO<>(
                invoiceDTOList,
                invoicePage.getTotalPages(),
                invoicePage.getTotalElements(),
                invoicePage.getNumber(),
                invoicePage.getSize()
        );
    }

    @Override
    public List<BookRespDTO> findBestSellersList() {
        List<InvoiceItem> invoiceItemPage = invoiceItemRepository.findBestSellersList();
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

    @Override
    public PageDTO<BookRespDTO> findBestSellersPage(int page, int size) {
        Page<InvoiceItem> invoiceItemPage = invoiceItemRepository.findBestSellersPage(PageRequest.of(page-1,size));
        //List<InvoiceItem> invoiceItemPage = invoiceItemRepository.findBestSellersList();
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
        //return bookRespDTOPageDTO;
        return new PageDTO<>(
                bookRespDTOPageDTO,
                invoiceItemPage.getTotalPages(),
                invoiceItemPage.getTotalElements(),
                invoiceItemPage.getNumber(),
                invoiceItemPage.getSize()
        );
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

    private List<InvoiceItemSearchDTO> getListItems(Long invoiceID){
        // InvoiceItem
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findByInvoiceId(invoiceID);
        List<InvoiceItemSearchDTO> invoiceItemDTOList = new ArrayList<>();
        for (InvoiceItem invoiceItem: invoiceItemList) {
            InvoiceItemSearchDTO invoiceItemSearchDTO = mapper.convertValue(invoiceItem, InvoiceItemSearchDTO.class);
            //TODO: Ver si incluir o no la info de cada Book, porque la consulta de invoices dura mas de 10 segundos.
            /*Optional<Book> optionalBook = bookRepository.findById(invoiceItem.getBookId());
            optionalBook.ifPresent(book -> invoiceItemSearchDTO.setBookDTO(mapper.convertValue(book, BookDTO.class)));*/
            invoiceItemDTOList.add(invoiceItemSearchDTO);
        }
        return invoiceItemDTOList;
    }

    private ClientDTO getUser(Long invoiceId) {
        ClientDTO clientDTO = null;
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(invoiceId);
        try {
            Long userId = optionalInvoice.get().getUserId();
            UserDTO userDTO = userService.findById(Math.toIntExact(userId));
            clientDTO = mapper.convertValue(userDTO, ClientDTO.class);
        } catch (Exception e) {

        }
        return clientDTO;
    }


}
