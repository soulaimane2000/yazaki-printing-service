package com.yazaki.printingservice.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.yazaki.printingservice.domain.model.PrintEvent;

public interface PrintEventRepository extends JpaRepository<PrintEvent, UUID> {

    List<PrintEvent> findAllByPrintJobIdOrderBySequenceNumberAsc(UUID printJobId);
    @Query("select coalesce(max(e.sequenceNumber),0) from PrintEvent e where e.printJob.id=:jobId") long maxSequence(UUID jobId);
}
