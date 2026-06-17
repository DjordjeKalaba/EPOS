package com.epos.provisioning_service.mq.events;

public class TenantProvisionedEvent {
    public Long tenantId;
    public String dbName;
    public boolean success;
    public String message;

    public TenantProvisionedEvent() {}

    public TenantProvisionedEvent(Long tenantId, String dbName, boolean success, String message) {
        this.tenantId = tenantId;
        this.dbName = dbName;
        this.success = success;
        this.message = message;
    }
}
