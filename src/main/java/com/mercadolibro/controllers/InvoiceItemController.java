package com.mercadolibro.controllers;

import com.mercadolibro.dto.InvoiceItemDTO;
import com.mercadolibro.entities.InvoiceItem;
import com.mercadolibro.service.InvoiceItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/invoiceitem")
public class InvoiceItemController {

    @Autowired
    InvoiceItemService invoiceItemService;

    @PostMapping
    public ResponseEntity<InvoiceItemDTO> save(@RequestBody InvoiceItem invoiceItem) {
        return ResponseEntity.ok(invoiceItemService.save(invoiceItem));
    }

    @GetMapping
    public ResponseEntity<InvoiceItemDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceItemService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<InvoiceItemDTO>> findAll() {
        return ResponseEntity.ok(invoiceItemService.findAll());
    }

}
