package com.epos.invoice_service.api.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class InvoiceCreateDto {
    @NotNull
    public Long clientId;

    public LocalDate issueDate; // optional

    @NotNull
    public List<InvoiceItemCreateDto> items;
}
