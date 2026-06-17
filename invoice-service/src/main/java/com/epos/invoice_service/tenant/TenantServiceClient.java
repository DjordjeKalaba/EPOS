package com.epos.invoice_service.tenant;

import com.epos.invoice_service.tenant.dto.TenantInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Component
public class TenantServiceClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public TenantServiceClient(RestTemplate restTemplate,
                               @Value("${app.tenant-service.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public TenantInfoDto getTenant(Long tenantId) {
        try {
            return restTemplate.getForObject(
                    baseUrl + "/api/internal/tenants/" + tenantId,
                    TenantInfoDto.class
            );
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid tenantId or tenant-service unavailable");
        }
    }
}
