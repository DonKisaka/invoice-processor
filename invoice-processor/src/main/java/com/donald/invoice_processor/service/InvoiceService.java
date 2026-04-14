package com.donald.invoice_processor.service;

import com.donald.invoice_processor.dto.InvoiceRequestDto;
import com.donald.invoice_processor.dto.InvoiceResponseDto;
import com.donald.invoice_processor.mapper.InvoiceMapper;
import com.donald.invoice_processor.model.Invoice;
import com.donald.invoice_processor.model.InvoiceStatus;
import com.donald.invoice_processor.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceExtractionService extractionService;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoicemapper;

    public InvoiceService(InvoiceExtractionService extractionService, InvoiceRepository invoiceRepository, InvoiceMapper invoicemapper) {
        this.extractionService = extractionService;
        this.invoiceRepository = invoiceRepository;
        this.invoicemapper = invoicemapper;
    }

    public InvoiceResponseDto process(InvoiceRequestDto request) {
        var extracted = extractionService.extract(request.rawText());

        Invoice invoice = invoicemapper.toEntity(extracted);
        invoice.setStatus(InvoiceStatus.PROCESSED);

        Invoice saved = invoiceRepository.save(invoice);
        return invoicemapper.toResponse(saved);
    }

    public List<InvoiceResponseDto> getAll() {
        return invoiceRepository.findAll().stream()
                .map(invoicemapper::toResponse)
                .toList();
    }

    public InvoiceResponseDto getById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found: " + id));
        return invoicemapper.toResponse(invoice);
    }
}
