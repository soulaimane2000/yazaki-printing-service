package com.yazaki.printingservice.domain.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.yazaki.printingservice.TestcontainersConfiguration;
import com.yazaki.printingservice.domain.model.PrintEvent;
import com.yazaki.printingservice.domain.model.PrintJob;
import com.yazaki.printingservice.domain.model.PrintJobStatus;
import com.yazaki.printingservice.domain.model.PrintRequest;
import com.yazaki.printingservice.domain.model.Printer;
import com.yazaki.printingservice.domain.model.PrinterType;

@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@SpringBootTest
@Transactional
class PrintingRepositoryIntegrationTest {

    @Autowired
    private PrinterRepository printerRepository;

    @Autowired
    private PrintRequestRepository printRequestRepository;

    @Autowired
    private PrintJobRepository printJobRepository;

    @Autowired
    private PrintEventRepository printEventRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void persistsRequestJobsPrinterAndOrderedEvents() {
        var printer = printerRepository.save(new Printer("TEST-LABEL-01", "Test label printer", PrinterType.LABEL));
        var request = printRequestRepository.save(
                new PrintRequest("test-request-001", Instant.parse("2026-01-01T10:00:00Z")));
        var originalJob = printJobRepository.save(new PrintJob(request, printer));
        var reprintJob = printJobRepository.save(new PrintJob(request, printer, originalJob));

        printEventRepository.save(new PrintEvent(
                originalJob,
                2,
                PrintJobStatus.VALIDATED,
                Instant.parse("2026-01-01T10:00:02Z"),
                "Validated test event"));
        printEventRepository.save(new PrintEvent(
                originalJob,
                1,
                PrintJobStatus.RECEIVED,
                Instant.parse("2026-01-01T10:00:01Z"),
                null));

        var jobs = printJobRepository.findAllByPrintRequestIdOrderByCreatedAtAsc(request.getId());
        var events = printEventRepository.findAllByPrintJobIdOrderBySequenceNumberAsc(originalJob.getId());

        assertEquals(2, jobs.size());
        assertEquals(originalJob.getId(), reprintJob.getReprintOfJob().getId());
        assertEquals(2, events.size());
        assertEquals(1, events.getFirst().getSequenceNumber());
        assertEquals(PrintJobStatus.RECEIVED, events.getFirst().getStatus());
        assertNotNull(originalJob.getCreatedAt());
        assertTrue(printerRepository.findByCode("TEST-LABEL-01").isPresent());
        assertTrue(printRequestRepository.findByRequestReference("test-request-001").isPresent());
    }

    @Test
    void rejectsDuplicateRequestReference() {
        var receivedAt = Instant.parse("2026-01-01T10:00:00Z");
        printRequestRepository.saveAndFlush(new PrintRequest("duplicate-reference", receivedAt));

        assertThrows(
                DataIntegrityViolationException.class,
                () -> printRequestRepository.saveAndFlush(new PrintRequest("duplicate-reference", receivedAt)));
    }

    @Test
    void rejectsPrintedJobWithoutPrintedConfirmationTimestamp() {
        var printer = printerRepository.saveAndFlush(
                new Printer("TEST-DOCUMENT-01", "Test document printer", PrinterType.DOCUMENT));
        var request = printRequestRepository.saveAndFlush(
                new PrintRequest("test-request-printed-constraint", Instant.parse("2026-01-01T10:00:00Z")));
        var now = OffsetDateTime.parse("2026-01-01T10:00:01Z");

        assertThrows(DataIntegrityViolationException.class, () -> jdbcTemplate.update(
                """
                INSERT INTO print_job (
                    id,
                    print_request_id,
                    printer_id,
                    status,
                    submitted_at,
                    printed_at,
                    version,
                    created_at,
                    updated_at
                ) VALUES (?, ?, ?, 'PRINTED', ?, NULL, 0, ?, ?)
                """,
                UUID.randomUUID(),
                request.getId(),
                printer.getId(),
                now,
                now,
                now));
    }
}
