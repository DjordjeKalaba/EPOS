package com.epos.invoice_service.api;

import com.epos.invoice_service.api.dto.InvoiceCreateDto;
import com.epos.invoice_service.domain.Invoice;
import com.epos.invoice_service.repo.InvoiceRepository;
import com.epos.invoice_service.service.InvoicePdfService;
import com.epos.invoice_service.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceRepository invoiceRepo;
    private final InvoiceService invoiceService;
    private final InvoicePdfService pdfService;

    public InvoiceController(InvoiceRepository invoiceRepo, InvoiceService invoiceService, InvoicePdfService pdfService) {
        this.invoiceRepo = invoiceRepo;
        this.invoiceService = invoiceService;
        this.pdfService = pdfService;
    }

    @GetMapping
    public List<Invoice> list() {
        return invoiceRepo.findAll();
    }

    @PostMapping
    public Invoice create(@Valid @RequestBody InvoiceCreateDto dto) {
        return invoiceService.create(dto);
    }

    @GetMapping("/{id}")
    public Invoice get(@PathVariable Long id) {
        return invoiceRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> pdf(@PathVariable Long id) {
        Invoice inv = invoiceRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));

        byte[] pdf = pdfService.generatePdf(inv);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"invoice-" + inv.getInvoiceNumber() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
