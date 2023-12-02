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
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Arrays;
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

    @GetMapping("/salesbypaymenttype")
    @ApiOperation(value = "Get sales count by payment type", notes = "Returns a list of sales count for each payment type.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sales count by payment type retrieved successfully", response = List.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public ResponseEntity<List<PaymentTypeSaleDTO>> getSalesByPaymentType() {
        return ResponseEntity.ok(invoiceRequestService.findSalesByPaymentType());
    }

    @GetMapping("/monthlystock")
    @ApiOperation(value = "Get monthly stock", notes = "Returns a paginated list of monthly stock data.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Monthly stock retrieved successfully", response = PageDTO.class),
            @ApiResponse(code = 400, message = "Bad request")
    })
    public ResponseEntity<PageDTO<MonthlyStockDTO>> getMonthlyStock(@RequestParam(defaultValue = "0") @PositiveOrZero int page, @RequestParam(defaultValue = "10") @Positive int size) {
        List<MonthlyStockDTO> monthlyStockList = Arrays.asList(
                new MonthlyStockDTO("2023", "Enero", 100L),
                new MonthlyStockDTO("2023", "Febrero", 120L),
                new MonthlyStockDTO("2023", "Marzo", 90L),
                new MonthlyStockDTO("2023", "Abril", 110L),
                new MonthlyStockDTO("2023", "Mayo", 80L),
                new MonthlyStockDTO("2023", "Junio", 130L),
                new MonthlyStockDTO("2023", "Julio", 95L),
                new MonthlyStockDTO("2023", "Agosto", 115L),
                new MonthlyStockDTO("2023", "Septiembre", 75L),
                new MonthlyStockDTO("2023", "Octubre", 105L),
                new MonthlyStockDTO("2023", "Noviembre", 88L),
                new MonthlyStockDTO("2023", "Diciembre", 125L)
        );

        int start = page * size;
        int end = Math.min((start + size), monthlyStockList.size());

        List<MonthlyStockDTO> content = monthlyStockList.subList(start, end);

        PageDTO<MonthlyStockDTO> pageDTO = new PageDTO<>(content, content.size(), (long) monthlyStockList.size(), page, size);

        return ResponseEntity.ok(pageDTO);
    }
}
