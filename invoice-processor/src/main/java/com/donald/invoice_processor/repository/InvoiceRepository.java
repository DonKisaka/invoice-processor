package com.donald.invoice_processor.repository;

import com.donald.invoice_processor.model.Invoice;
import com.donald.invoice_processor.model.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByVendorContainingIgnoreCase(String vendor);
    List<Invoice> findByStatus(InvoiceStatus status);
}
