package com.yazaki.printingservice.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yazaki.printingservice.domain.model.PrintEvent;

public interface PrintEventRepository extends JpaRepository<PrintEvent, UUID> {

    List<PrintEvent> findAllByPrintJobIdOrderBySequenceNumberAsc(UUID printJobId);
}
