package com.epos.invoice_service.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class InvoiceItemCreateDto {
    @NotBlank
    public String description;

    @NotNull
    public BigDecimal quantity;

    @NotNull
    public BigDecimal unitPrice;
}
