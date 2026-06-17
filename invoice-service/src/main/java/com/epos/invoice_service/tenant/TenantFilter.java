package com.epos.invoice_service.tenant;

import com.epos.invoice_service.tenant.dto.TenantInfoDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TenantFilter extends OncePerRequestFilter {

    private final TenantServiceClient tenantServiceClient;

    public TenantFilter(TenantServiceClient tenantServiceClient) {
        this.tenantServiceClient = tenantServiceClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String tenantIdHeader = request.getHeader("X-Tenant-Id");

        if (tenantIdHeader == null || tenantIdHeader.isBlank()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write("Missing header: X-Tenant-Id");
            return;
        }

        Long tenantId;
        try {
            tenantId = Long.parseLong(tenantIdHeader);
        } catch (NumberFormatException ex) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write("Invalid X-Tenant-Id");
            return;
        }

        TenantInfoDto tenant = tenantServiceClient.getTenant(tenantId);
        if (tenant == null || tenant.dbName == null || tenant.dbName.isBlank()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write("Tenant has no dbName");
            return;
        }

        if (!"READY".equalsIgnoreCase(tenant.status)) {
            response.setStatus(HttpStatus.CONFLICT.value());
            response.getWriter().write("Tenant is not READY (status=" + tenant.status + ")");
            return;
        }

        try {
            TenantContext.setDbName(tenant.dbName);
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
