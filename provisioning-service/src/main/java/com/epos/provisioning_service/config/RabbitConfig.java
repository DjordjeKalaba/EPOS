package com.epos.provisioning_service.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "epos.tenant.exchange";

    public static final String QUEUE_TENANT_APPROVED = "epos.tenant.approved.q";
    public static final String RK_TENANT_APPROVED = "tenant.approved";

    public static final String QUEUE_TENANT_PROVISIONED = "epos.tenant.provisioned.q";
    public static final String RK_TENANT_PROVISIONED = "tenant.provisioned";

    @Bean
    public TopicExchange tenantExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue tenantApprovedQueue() {
        return QueueBuilder.durable(QUEUE_TENANT_APPROVED).build();
    }

    @Bean
    public Binding tenantApprovedBinding(TopicExchange tenantExchange, Queue tenantApprovedQueue) {
        return BindingBuilder.bind(tenantApprovedQueue).to(tenantExchange).with(RK_TENANT_APPROVED);
    }

    @Bean
    public Queue tenantProvisionedQueue() {
        return QueueBuilder.durable(QUEUE_TENANT_PROVISIONED).build();
    }

    @Bean
    public Binding tenantProvisionedBinding(TopicExchange tenantExchange, Queue tenantProvisionedQueue) {
        return BindingBuilder.bind(tenantProvisionedQueue).to(tenantExchange).with(RK_TENANT_PROVISIONED);
    }
}
