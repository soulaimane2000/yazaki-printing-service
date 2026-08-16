package com.yazaki.printingservice.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yazaki.printingservice.domain.model.PrintRequest;

public interface PrintRequestRepository extends JpaRepository<PrintRequest, UUID> {

    Optional<PrintRequest> findByRequestReference(String requestReference);
}
