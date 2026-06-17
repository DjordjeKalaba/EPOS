package com.epos.tenant_service.mq.events;

public class TenantProvisionedEvent {
    public Long tenantId;
    public String dbName;
    public boolean success;
    public String message;

    public TenantProvisionedEvent() {}
}
