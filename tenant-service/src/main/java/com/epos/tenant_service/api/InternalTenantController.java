package com.epos.tenant_service.api;

import com.epos.tenant_service.api.dto.TenantInfoDto;
import com.epos.tenant_service.domain.Tenant;
import com.epos.tenant_service.repo.TenantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/internal/tenants")
public class InternalTenantController {

    private final TenantRepository tenantRepository;

    public InternalTenantController(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @GetMapping("/{tenantId}")
    public TenantInfoDto getTenant(@PathVariable Long tenantId) {
        Tenant t = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tenant not found"));

        return new TenantInfoDto(t.getId(), t.getCompanyName(), t.getDbName(), t.getStatus());
    }
}
