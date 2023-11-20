package com.mercadolibro.controller;

import com.mercadolibro.dto.BookRespDTO;
import com.mercadolibro.dto.InvoiceRequestDTO;
import com.mercadolibro.dto.MonthlySaleDTO;
import com.mercadolibro.dto.PageDTO;
import com.mercadolibro.entity.InvoiceRequest;
import com.mercadolibro.service.InvoiceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    InvoiceRequestService invoiceRequestService;

    @PostMapping
    public ResponseEntity<InvoiceRequestDTO> save(@RequestBody InvoiceRequest invoiceRequest) {
        return ResponseEntity.ok(invoiceRequestService.save(invoiceRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceRequestDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceRequestService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<PageDTO<InvoiceRequestDTO>> findAll(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(invoiceRequestService.findAll(page, size));
    }

    @GetMapping("/userid/{userId}")
    public ResponseEntity<PageDTO<InvoiceRequestDTO>> findByUserId(@PathVariable Long userId , @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(invoiceRequestService.findByUserId(userId, page, size));
    }

    @GetMapping("/bestsellers")
    public ResponseEntity<List<BookRespDTO>> findBestSellers() {
        return ResponseEntity.ok(invoiceRequestService.findBestSellers());
    }

    @GetMapping("/monthlysales")
    public ResponseEntity<PageDTO<MonthlySaleDTO>> getMonthlySales(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(invoiceRequestService.getMonthlySales(page, size));
    }

}
