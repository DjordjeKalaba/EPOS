package com.epos.auth_service.repo;

import com.epos.auth_service.domain.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {}
