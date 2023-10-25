package com.mercadolibro.controllers;

import com.mercadolibro.dto.InvoiceDTO;
import com.mercadolibro.entities.Invoice;
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
    public ResponseEntity<InvoiceDTO> save(@RequestBody Invoice invoice) {
        return ResponseEntity.ok(invoiceService.save(invoice));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<InvoiceDTO>> findAll() {
        return ResponseEntity.ok(invoiceService.findAll());
    }

}
