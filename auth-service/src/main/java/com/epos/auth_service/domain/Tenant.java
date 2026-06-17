package com.epos.auth_service.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "tenants")
public class Tenant {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name", nullable = false, length = 150)
    private String companyName;

    @Column(name = "db_name", length = 200)
    private String dbName;

    @Column(nullable = false, length = 30)
    private String status;

    public Long getId() { return id; }
    public String getCompanyName() { return companyName; }
    public String getDbName() { return dbName; }
    public String getStatus() { return status; }
}
