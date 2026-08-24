package com.yazaki.printingservice.domain.repository;
import java.util.*; import org.springframework.data.jpa.repository.JpaRepository; import com.yazaki.printingservice.domain.model.PrintAttempt;
public interface PrintAttemptRepository extends JpaRepository<PrintAttempt, UUID> { List<PrintAttempt> findByPrintJobIdOrderByAttemptNumber(UUID jobId); }
