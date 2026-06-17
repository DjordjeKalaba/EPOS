package com.epos.invoice_service.repo;

import com.epos.invoice_service.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {}
