package com.yazaki.printingservice.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yazaki.printingservice.domain.model.Printer;

public interface PrinterRepository extends JpaRepository<Printer, UUID> {

    Optional<Printer> findByCode(String code);
}
