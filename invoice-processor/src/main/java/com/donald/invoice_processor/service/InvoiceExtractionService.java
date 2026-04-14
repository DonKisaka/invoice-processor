package com.donald.invoice_processor.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class InvoiceExtractionService {
    private final ChatClient chatClient;

    public InvoiceExtractionService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public ExtractedInvoice extract(String rawText) {
        String prompt = """
                  You are an invoice data extraction expert.
                  Extract all invoice details from the text below and return structured data.
                  If a field is missing, return null for that field.

                  Invoice Text:
                  %s
                  """.formatted(rawText);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .entity(ExtractedInvoice.class);
    }

    public record ExtractedInvoice(
            String vendor,
            String vendorEmail,
            String vendorAddress,
            String invoiceNumber,
            LocalDate invoiceDate,
            LocalDate dueDate,
            BigDecimal totalAmount,
            String currency,
            String customerName,
            String customerAddress,
            List<LineItem> lineItems
    ) {
        public record LineItem(
                String description,
                Integer quantity,
                BigDecimal unitPrice,
                BigDecimal totalPrice
        ) {}
    }
}
