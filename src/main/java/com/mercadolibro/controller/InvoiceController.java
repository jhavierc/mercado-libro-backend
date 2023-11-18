package com.mercadolibro.controller;

import com.mercadolibro.dto.InvoiceRequestDTO;
import com.mercadolibro.dto.PageDTO;
import com.mercadolibro.dto.PaymentReqDTO;
import com.mercadolibro.dto.PaymentRespDTO;
import com.mercadolibro.entity.InvoiceRequest;
import com.mercadolibro.service.InvoiceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

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

    @PostMapping("/{id}/payment")
    public ResponseEntity<PaymentRespDTO> processPayment(@PathVariable UUID id, @RequestBody @Valid PaymentReqDTO paymentReqDTO) {
        return ResponseEntity.ok(invoiceRequestService.processPayment(id, paymentReqDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceRequestDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(invoiceRequestService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<InvoiceRequestDTO>> findAll() {
        return ResponseEntity.ok(invoiceRequestService.findAll());
    }


    @GetMapping
    public ResponseEntity<PageDTO<InvoiceRequestDTO>> findAll(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(invoiceRequestService.findAll(page, size));
    }

}
