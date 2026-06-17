package com.epos.invoice_service.tenant;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {

    private final TenantDataSourceProvider provider;

    public RoutingDataSource(TenantDataSourceProvider provider) {
        this.provider = provider;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String dbName = TenantContext.getDbName();
        if (dbName == null || dbName.isBlank()) return "epos_master";
        return dbName;
    }

    @Override
    protected DataSource determineTargetDataSource() {
        String key = (String) determineCurrentLookupKey();
        return provider.getOrCreate(key);
    }
}
