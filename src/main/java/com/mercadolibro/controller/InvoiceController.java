package com.mercadolibro.controller;

import com.mercadolibro.dto.*;
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
@RequestMapping("/api/invoice")
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
    public ResponseEntity<InvoiceSearchDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(invoiceRequestService.findById(id));
    }

    @GetMapping("")
    public ResponseEntity<List<InvoiceSearchDTO>> findAll() {
        return ResponseEntity.ok(invoiceRequestService.findAll());
    }

    @GetMapping("/all")
    public ResponseEntity<PageDTO<InvoiceSearchDTO>> findAll(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(invoiceRequestService.findAll(page, size));
    }

    @GetMapping("/userid/{userId}")
    public ResponseEntity<PageDTO<InvoiceSearchDTO>> findByUserId(@PathVariable Long userId , @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(invoiceRequestService.findByUserId(userId, page, size));
    }

    @GetMapping("/bestsellers/list")
    public ResponseEntity<List<BookRespDTO>> findBestSellersList() {
        return ResponseEntity.ok(invoiceRequestService.findBestSellersList());
    }

    @GetMapping("/bestsellers/page")
    public ResponseEntity<PageDTO<BookRespDTO>> findBestSellersPage(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(invoiceRequestService.findBestSellersPage(page, size));
    }

    @GetMapping("/monthlysales")
    public ResponseEntity<PageDTO<MonthlySaleDTO>> getMonthlySales(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(invoiceRequestService.getMonthlySales(page, size));
    }

    @GetMapping("/salesbycategory")
    public ResponseEntity<PageDTO<CategorySalesDTO>> getSalesByCategory(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(invoiceRequestService.getSalesByCategory(page, size));
    }
}
