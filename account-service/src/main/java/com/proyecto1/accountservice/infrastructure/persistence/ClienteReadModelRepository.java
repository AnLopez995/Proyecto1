package com.proyecto1.accountservice.infrastructure.persistence;

import com.proyecto1.accountservice.domain.model.ClienteReadModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteReadModelRepository extends JpaRepository<ClienteReadModel, Long> {
}