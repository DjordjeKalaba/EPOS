package com.epos.tenant_service.mq;

import com.epos.tenant_service.config.RabbitConfig;
import com.epos.tenant_service.domain.Tenant;
import com.epos.tenant_service.domain.TenantStatus;
import com.epos.tenant_service.mq.events.TenantProvisionedEvent;
import com.epos.tenant_service.repo.TenantRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TenantProvisionedConsumer {

    private final TenantRepository tenantRepository;

    public TenantProvisionedConsumer(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_TENANT_PROVISIONED)
    public void onMessage(TenantProvisionedEvent event) {
        Tenant t = tenantRepository.findById(event.tenantId)
                .orElseThrow(() -> new IllegalStateException("Tenant not found: " + event.tenantId));

        if (event.success) {
            t.setStatus(TenantStatus.READY);
        } else {
            t.setStatus(TenantStatus.FAILED);
        }

        tenantRepository.save(t);

        System.out.println("Tenant " + t.getId() + " (" + t.getDbName() + ") status -> " + t.getStatus()
                + (event.message != null ? (" | message=" + event.message) : ""));
    }
}
