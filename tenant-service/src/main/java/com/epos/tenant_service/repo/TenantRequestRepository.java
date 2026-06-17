package com.epos.tenant_service.repo;

import com.epos.tenant_service.domain.TenantRequest;
import com.epos.tenant_service.domain.TenantRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TenantRequestRepository extends JpaRepository<TenantRequest, Long> {
    List<TenantRequest> findByStatusOrderByIdDesc(TenantRequestStatus status);
}