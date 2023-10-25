package com.mercadolibro.controllers;

import com.mercadolibro.dto.InvoiceRequestDTO;
import com.mercadolibro.entities.InvoiceRequest;
import com.mercadolibro.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<InvoiceRequestDTO> save(@RequestBody InvoiceRequest invoiceRequest) {
        return ResponseEntity.ok(invoiceService.save(invoiceRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceRequestDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<InvoiceRequestDTO>> findAll() {
        return ResponseEntity.ok(invoiceService.findAll());
    }

}
