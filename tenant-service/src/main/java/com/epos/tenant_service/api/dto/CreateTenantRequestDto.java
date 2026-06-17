package com.epos.tenant_service.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CreateTenantRequestDto {

    @NotBlank
    public String companyName;

    @NotBlank
    public String contactName;

    @NotBlank
    @Email
    public String email;

    public String phone;
}