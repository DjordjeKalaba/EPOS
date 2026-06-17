package com.epos.provisioning_service.mq.events;

public class TenantApprovedEvent {
    public Long tenantId;
    public String dbName;

    public TenantApprovedEvent() {}
}
