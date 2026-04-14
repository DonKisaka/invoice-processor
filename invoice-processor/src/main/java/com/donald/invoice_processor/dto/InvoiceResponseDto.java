package com.donald.invoice_processor.dto;

import com.donald.invoice_processor.model.Invoice;
import com.donald.invoice_processor.model.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record InvoiceResponseDto(
        Long id,
        String vendor,
        String VendorEmail,
        String vendorAddress,
        String invoiceNumber,
        LocalDate invoiceDate,
        LocalDate dueDate,
        BigDecimal totalAmount,
        String currency,
        String customer,
        String customerAddress,
        List<Invoice.LineItem> lineItems,
        InvoiceStatus status,
        LocalDateTime processedAt
) {}
