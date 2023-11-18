package com.mercadolibro.controller;

import com.mercadolibro.dto.InvoiceRequestDTO;
import com.mercadolibro.dto.PageDTO;
import com.mercadolibro.dto.PaymentReqDTO;
import com.mercadolibro.dto.PaymentRespDTO;
import com.mercadolibro.entity.InvoiceRequest;
import com.mercadolibro.exception.InvoicePaymentException;
import com.mercadolibro.service.InvoiceRequestService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @ApiOperation(value = "Process a payment for a specific invoice")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Payment processed successfully"),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 404, message = "Invoice not found"),
            @ApiResponse(code = 409, message = "Bad payment status", response = InvoicePaymentException.class),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/{id}/payment")
    public ResponseEntity<PaymentRespDTO> processPayment(@PathVariable UUID id, @RequestBody @Valid PaymentReqDTO paymentReqDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceRequestService.processPayment(id, paymentReqDTO));
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
