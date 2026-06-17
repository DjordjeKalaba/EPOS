package com.epos.invoice_service.tenant;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TenantDataSourceProvider {

    private final ConcurrentHashMap<String, DataSource> cache = new ConcurrentHashMap<>();

    public DataSource getOrCreate(String dbName) {
        return cache.computeIfAbsent(dbName, this::create);
    }

    private DataSource create(String dbName) {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/" + dbName)
                .username("epos")
                .password("epos")
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
