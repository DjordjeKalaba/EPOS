package com.epos.tenant_service.api;

import com.epos.tenant_service.config.RabbitConfig;
import com.epos.tenant_service.domain.Tenant;
import com.epos.tenant_service.domain.TenantRequest;
import com.epos.tenant_service.domain.TenantRequestStatus;
import com.epos.tenant_service.domain.TenantStatus;
import com.epos.tenant_service.mq.events.TenantApprovedEvent;
import com.epos.tenant_service.repo.TenantRepository;
import com.epos.tenant_service.repo.TenantRequestRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tenant-requests")
public class AdminTenantRequestController {

    private final TenantRequestRepository requestRepo;
    private final TenantRepository tenantRepo;
    private final RabbitTemplate rabbitTemplate;

    public AdminTenantRequestController(TenantRequestRepository requestRepo,
                                        TenantRepository tenantRepo,
                                        RabbitTemplate rabbitTemplate) {
        this.requestRepo = requestRepo;
        this.tenantRepo = tenantRepo;
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping
    public List<TenantRequest> list(@RequestParam(defaultValue = "PENDING") TenantRequestStatus status) {
        return requestRepo.findByStatusOrderByIdDesc(status);
    }

    @PostMapping("/{id}/approve")
    public Tenant approve(@PathVariable Long id) {
        TenantRequest req = requestRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TenantRequest not found"));

        if (req.getStatus() != TenantRequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is not PENDING");
        }

        req.setStatus(TenantRequestStatus.APPROVED);
        requestRepo.save(req);

        Tenant t = new Tenant();
        t.setCompanyName(req.getCompanyName());

        // Za sada (Korak 1) samo generišemo ime baze, ali NE kreiramo DB još.
        // Kreiranje DB ide u provisioning-service u sljedećem koraku.
        String dbName = "epos_tenant_" + slug(req.getCompanyName());
        t.setDbName(dbName);
        t.setStatus(TenantStatus.PROVISIONING);

        Tenant saved = tenantRepo.save(t);

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.RK_TENANT_APPROVED,
                new TenantApprovedEvent(saved.getId(), saved.getDbName())
        );

        return saved;
    }

    @PostMapping("/{id}/reject")
    public TenantRequest reject(@PathVariable Long id) {
        TenantRequest req = requestRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TenantRequest not found"));

        if (req.getStatus() != TenantRequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is not PENDING");
        }

        req.setStatus(TenantRequestStatus.REJECTED);
        return requestRepo.save(req);
    }


    private String slug(String s) {
        return s.toLowerCase()
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_+|_+$", "");
    }
}
