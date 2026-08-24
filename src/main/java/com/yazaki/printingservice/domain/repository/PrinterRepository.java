package com.yazaki.printingservice.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.yazaki.printingservice.domain.model.Printer;

public interface PrinterRepository extends JpaRepository<Printer, UUID>, JpaSpecificationExecutor<Printer> {

    Optional<Printer> findByCode(String code);
    long countByStatusAndEnabledTrue(com.yazaki.printingservice.domain.model.PrinterStatus status);
}
