package com.epos.tenant_service.api;

import com.epos.tenant_service.api.dto.CreateTenantRequestDto;
import com.epos.tenant_service.domain.TenantRequest;
import com.epos.tenant_service.repo.TenantRequestRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/tenant-requests")
public class PublicTenantRequestController {

    private final TenantRequestRepository repo;

    public PublicTenantRequestController(TenantRequestRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TenantRequest create(@Valid @RequestBody CreateTenantRequestDto dto) {
        TenantRequest tr = new TenantRequest();
        tr.setCompanyName(dto.companyName);
        tr.setContactName(dto.contactName);
        tr.setEmail(dto.email);
        tr.setPhone(dto.phone);
        return repo.save(tr);
    }
}
