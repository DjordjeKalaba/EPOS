package com.epos.client_service.tenant;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {

    private final TenantDataSourceProvider provider;

    public RoutingDataSource(TenantDataSourceProvider provider) {
        this.provider = provider;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        // kljuc = dbName (npr. epos_tenant_firma_test)
        String dbName = TenantContext.getDbName();
        if (dbName == null || dbName.isBlank()) {
            return "epos_master";
        }
        return dbName;
    }

    @Override
    protected DataSource determineTargetDataSource() {
        String key = (String) determineCurrentLookupKey();

        // ako je master -> vrati master
        if ("epos_master".equals(key)) {
            return provider.getOrCreate("epos_master");
        }

        // ako je tenant -> vrati tenant datasource
        return provider.getOrCreate(key);
    }
}
