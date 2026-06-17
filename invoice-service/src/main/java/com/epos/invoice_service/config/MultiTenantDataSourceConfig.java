package com.epos.invoice_service.config;

import com.epos.invoice_service.tenant.RoutingDataSource;
import com.epos.invoice_service.tenant.TenantDataSourceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultiTenantDataSourceConfig {

    @Bean
    public DataSource dataSource(TenantDataSourceProvider provider) {
        Map<Object, Object> targets = new HashMap<>();
        targets.put("epos_master", provider.getOrCreate("epos_master"));

        RoutingDataSource rds = new RoutingDataSource(provider);
        rds.setTargetDataSources(targets);
        rds.setDefaultTargetDataSource(provider.getOrCreate("epos_master"));
        rds.afterPropertiesSet();
        return rds;
    }
}
