package com.epos.invoice_service.repo;

import com.epos.invoice_service.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {}
