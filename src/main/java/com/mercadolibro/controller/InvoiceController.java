package com.mercadolibro.controller;

import com.mercadolibro.dto.InvoiceRequestDTO;
import com.mercadolibro.entity.InvoiceRequest;
import com.mercadolibro.service.InvoiceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<InvoiceRequestDTO>> findAll() {
        return ResponseEntity.ok(invoiceRequestService.findAll());
    }

    @GetMapping
    public ResponseEntity<List<InvoiceRequestDTO>> findAll(@RequestParam int page, @RequestParam int size, @RequestParam Boolean sortAsc, @RequestParam String column) {
        return ResponseEntity.ok(invoiceRequestService.findAll(page, size, sortAsc, column));
    }

}
