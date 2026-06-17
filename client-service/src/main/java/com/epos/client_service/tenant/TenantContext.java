package com.epos.client_service.tenant;

public class TenantContext {
    private static final ThreadLocal<String> CURRENT_DB = new ThreadLocal<>();

    public static void setDbName(String dbName) {
        CURRENT_DB.set(dbName);
    }

    public static String getDbName() {
        return CURRENT_DB.get();
    }

    public static void clear() {
        CURRENT_DB.remove();
    }
}
