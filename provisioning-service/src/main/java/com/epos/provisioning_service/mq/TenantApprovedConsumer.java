package com.epos.provisioning_service.mq;

import com.epos.provisioning_service.config.RabbitConfig;
import com.epos.provisioning_service.mq.events.TenantApprovedEvent;
import com.epos.provisioning_service.mq.events.TenantProvisionedEvent;
import org.flywaydb.core.Flyway;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TenantApprovedConsumer {

    private final JdbcTemplate jdbcTemplate;
    private final RabbitTemplate rabbitTemplate;

    public TenantApprovedConsumer(JdbcTemplate jdbcTemplate, RabbitTemplate rabbitTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_TENANT_APPROVED)
    public void onMessage(TenantApprovedEvent event) {
        System.out.println("Received TenantApproved: tenantId=" + event.tenantId + ", dbName=" + event.dbName);

        try {
            // 1) provjeri postoji li baza
            Integer exists = jdbcTemplate.queryForObject(
                    "select count(*) from pg_database where datname = ?",
                    Integer.class,
                    event.dbName
            );

            if (exists != null && exists > 0) {
                System.out.println("DB already exists: " + event.dbName);
            } else {
                // 2) kreiraj bazu
                jdbcTemplate.execute("create database " + safeDbName(event.dbName));
                System.out.println("Created DB: " + event.dbName);

                // 3) pokreni Flyway migracije nad tenant bazom
                Flyway flyway = Flyway.configure()
                        .dataSource("jdbc:postgresql://localhost:5432/" + event.dbName, "epos", "epos")
                        .locations("classpath:db/migration/tenant")
                        .baselineOnMigrate(true)
                        .load();

                flyway.migrate();
                System.out.println("Migrated tenant DB: " + event.dbName);
            }

            // 4) pošalji "provisioned" event (success)
            rabbitTemplate.convertAndSend(
                    RabbitConfig.EXCHANGE,
                    RabbitConfig.RK_TENANT_PROVISIONED,
                    new TenantProvisionedEvent(event.tenantId, event.dbName, true, "OK")
            );

        } catch (Exception ex) {
            System.out.println("Provisioning FAILED for dbName=" + event.dbName + " : " + ex.getMessage());

            // pošalji "provisioned" event (failed)
            rabbitTemplate.convertAndSend(
                    RabbitConfig.EXCHANGE,
                    RabbitConfig.RK_TENANT_PROVISIONED,
                    new TenantProvisionedEvent(event.tenantId, event.dbName, false, ex.getMessage())
            );
        }
    }

    private String safeDbName(String dbName) {
        if (!dbName.matches("[a-zA-Z0-9_]+")) {
            throw new IllegalArgumentException("Invalid dbName: " + dbName);
        }
        return dbName;
    }
}
