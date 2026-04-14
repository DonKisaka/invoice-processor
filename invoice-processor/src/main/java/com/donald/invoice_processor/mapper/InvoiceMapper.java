package com.donald.invoice_processor.mapper;

import com.donald.invoice_processor.dto.InvoiceResponseDto;
import com.donald.invoice_processor.model.Invoice;
import com.donald.invoice_processor.service.InvoiceExtractionService;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    Invoice toEntity(InvoiceExtractionService.ExtractedInvoice extracted);

    InvoiceResponseDto toResponse(Invoice invoice);

    Invoice.LineItem toLineItem(InvoiceExtractionService.ExtractedInvoice.LineItem lineItem);
}
