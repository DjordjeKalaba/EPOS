package com.epos.tenant_service.repo;

import com.epos.tenant_service.domain.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
}