package com.donald.invoice_processor.controller;

import com.donald.invoice_processor.dto.InvoiceRequestDto;
import com.donald.invoice_processor.dto.InvoiceResponseDto;
import com.donald.invoice_processor.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {


    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/process")
    public ResponseEntity<InvoiceResponseDto> process(@RequestBody InvoiceRequestDto request) {
        InvoiceResponseDto response = invoiceService.process(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponseDto>> getAll() {
        return ResponseEntity.ok(invoiceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getById(id));
    }
}
