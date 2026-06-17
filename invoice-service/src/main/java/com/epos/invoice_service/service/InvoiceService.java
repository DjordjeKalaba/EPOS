package com.epos.invoice_service.service;

import com.epos.invoice_service.api.dto.InvoiceCreateDto;
import com.epos.invoice_service.domain.Invoice;
import com.epos.invoice_service.domain.InvoiceItem;
import com.epos.invoice_service.repo.ClientRepository;
import com.epos.invoice_service.repo.InvoiceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepo;
    private final ClientRepository clientRepo;

    public InvoiceService(InvoiceRepository invoiceRepo, ClientRepository clientRepo) {
        this.invoiceRepo = invoiceRepo;
        this.clientRepo = clientRepo;
    }

    public Invoice create(InvoiceCreateDto dto) {
        if (!clientRepo.existsById(dto.clientId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client does not exist");
        }
        if (dto.items == null || dto.items.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invoice items required");
        }

        Invoice inv = new Invoice();
        inv.setClientId(dto.clientId);
        inv.setIssueDate(dto.issueDate != null ? dto.issueDate : LocalDate.now());
        inv.setInvoiceNumber(generateInvoiceNumber());

        BigDecimal total = BigDecimal.ZERO;

        for (var it : dto.items) {
            if (it.quantity == null || it.unitPrice == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid item");
            }
            InvoiceItem item = new InvoiceItem();
            item.setInvoice(inv);
            item.setDescription(it.description);

            BigDecimal lineTotal = it.unitPrice.multiply(it.quantity);
            item.setQuantity(it.quantity);
            item.setUnitPrice(it.unitPrice);
            item.setLineTotal(lineTotal);

            inv.getItems().add(item);
            total = total.add(lineTotal);
        }

        inv.setTotalAmount(total);
        return invoiceRepo.save(inv);
    }

    private String generateInvoiceNumber() {
        // jednostavno i dovoljno za projekat
        return "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
