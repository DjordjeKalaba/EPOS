package com.epos.tenant_service.mq.events;

public class TenantApprovedEvent {
    public Long tenantId;
    public String dbName;

    public TenantApprovedEvent() {}

    public TenantApprovedEvent(Long tenantId, String dbName) {
        this.tenantId = tenantId;
        this.dbName = dbName;
    }
}
