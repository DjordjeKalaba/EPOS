package com.epos.tenant_service.api.dto;

import com.epos.tenant_service.domain.TenantStatus;

public class TenantInfoDto {
    public Long id;
    public String companyName;
    public String dbName;
    public TenantStatus status;

    public TenantInfoDto(Long id, String companyName, String dbName, TenantStatus status) {
        this.id = id;
        this.companyName = companyName;
        this.dbName = dbName;
        this.status = status;
    }
}
