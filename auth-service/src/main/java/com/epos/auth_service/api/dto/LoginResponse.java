package com.epos.auth_service.api.dto;

public record LoginResponse(
        String token,
        String role,
        Long tenantId
) {}
