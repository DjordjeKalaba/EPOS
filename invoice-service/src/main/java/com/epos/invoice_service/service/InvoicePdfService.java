package com.epos.invoice_service.service;

import com.epos.invoice_service.domain.Client;
import com.epos.invoice_service.domain.Invoice;
import com.epos.invoice_service.repo.ClientRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

@Service
public class InvoicePdfService {

    private final ClientRepository clientRepo;

    public InvoicePdfService(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    public byte[] generatePdf(Invoice invoice) {
        Client client = clientRepo.findById(invoice.getClientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client not found"));

        try (PDDocument doc = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PDPage page = new PDPage();
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {

                float y = 750;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
                cs.newLineAtOffset(50, y);
                cs.showText("INVOICE");
                cs.endText();

                y -= 30;

                y = writeLine(cs, y, "Invoice No: " + invoice.getInvoiceNumber());
                y = writeLine(cs, y, "Issue date: " + invoice.getIssueDate());
                y -= 10;

                y = writeLine(cs, y, "Bill to: " + safe(client.getName()));
                if (client.getEmail() != null) y = writeLine(cs, y, "Email: " + client.getEmail());
                if (client.getPhone() != null) y = writeLine(cs, y, "Phone: " + client.getPhone());
                if (client.getAddress() != null) y = writeLine(cs, y, "Address: " + client.getAddress());

                y -= 20;
                y = writeLineBold(cs, y, "Items:");
                y -= 5;

                int i = 1;
                for (var item : invoice.getItems()) {
                    BigDecimal qty = item.getQuantity();
                    BigDecimal price = item.getUnitPrice();
                    BigDecimal lt = item.getLineTotal();

                    y = writeLine(cs, y, i + ") " + item.getDescription());
                    y = writeLine(cs, y, "    qty=" + qty + "  unit=" + price + "  total=" + lt);
                    y -= 5;
                    i++;
                    if (y < 120) break; // jednostavno (bez multipage) za projekat
                }

                y -= 10;
                y = writeLineBold(cs, y, "TOTAL: " + invoice.getTotalAmount());
            }

            doc.save(out);
            return out.toByteArray();
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "PDF generation failed");
        }
    }

    private float writeLine(PDPageContentStream cs, float y, String text) throws IOException {
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA, 12);
        cs.newLineAtOffset(50, y);
        cs.showText(text);
        cs.endText();
        return y - 16;
    }

    private float writeLineBold(PDPageContentStream cs, float y, String text) throws IOException {
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
        cs.newLineAtOffset(50, y);
        cs.showText(text);
        cs.endText();
        return y - 16;
    }

    private String safe(String s) { return s == null ? "" : s; }
}
