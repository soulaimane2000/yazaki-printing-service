package com.yazaki.printingservice.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yazaki.printingservice.domain.model.PrintJob;
import com.yazaki.printingservice.domain.model.PrintJobStatus;

public interface PrintJobRepository extends JpaRepository<PrintJob, UUID> {

    List<PrintJob> findAllByPrintRequestIdOrderByCreatedAtAsc(UUID printRequestId);

    List<PrintJob> findAllByPrinterIdAndStatus(UUID printerId, PrintJobStatus status);
}
