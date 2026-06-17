package com.epos.client_service.api.dto;

import jakarta.validation.constraints.NotBlank;

public class ClientUpsertDto {
    @NotBlank
    public String name;

    public String email;
    public String phone;
    public String address;
}
