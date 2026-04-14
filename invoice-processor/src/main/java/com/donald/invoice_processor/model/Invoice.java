package com.donald.invoice_processor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "invoices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vendor;
    private String vendorEmail;
    private String vendorAddress;
    private String customer;
    private String customerAddress;
    private String customerEmail;
    private String invoiceNumber;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private BigDecimal totalAmount;
    private String currency;

    @ElementCollection
    @CollectionTable(name = "invoice_line_items", joinColumns = @JoinColumn(name = "invoice_id"))
    private List<LineItem> lineItems;

    private LocalDateTime processedAt;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @PrePersist
    protected void onCreate() {
        processedAt = LocalDateTime.now();
    }

    @Embeddable
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LineItem {
        private String description;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
    }
}
