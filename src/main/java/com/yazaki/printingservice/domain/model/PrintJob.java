package com.yazaki.printingservice.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "print_job")
public class PrintJob extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "print_request_id", nullable = false)
    private PrintRequest printRequest;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "printer_id", nullable = false)
    private Printer printer;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private PrintJobStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reprint_of_job_id")
    private PrintJob reprintOfJob;

    @Column(name = "submitted_at")
    private Instant submittedAt;

    @Column(name = "printed_at")
    private Instant printedAt;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;
    @Column(name = "max_retries", nullable = false)
    private int maxRetries;
    @Column(name = "next_attempt_at")
    private Instant nextAttemptAt;
    @Column(name = "processing_started_at")
    private Instant processingStartedAt;
    @Column(name = "completed_at")
    private Instant completedAt;
    @Column(name = "error_code", length = 64)
    private String errorCode;
    @Column(name = "error_message", length = 1000)
    private String errorMessage;
    @Column(name = "worker_id", length = 128)
    private String workerId;

    @OneToMany(mappedBy = "printJob")
    private List<PrintEvent> printEvents = new ArrayList<>();

    @Version
    @Column(nullable = false)
    private long version;

    protected PrintJob() {
    }

    public PrintJob(PrintRequest printRequest, Printer printer) {
        this(printRequest, printer, null);
    }

    public PrintJob(PrintRequest printRequest, Printer printer, PrintJob reprintOfJob) {
        this.printRequest = printRequest;
        this.printer = printer;
        this.reprintOfJob = reprintOfJob;
        this.status = PrintJobStatus.RECEIVED;
        this.maxRetries = 3;
        printRequest.attachJob(this);
    }

    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    public void transitionTo(PrintJobStatus newStatus, Instant now, String errorCode, String errorMessage) {
        this.status = newStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        if (newStatus == PrintJobStatus.RECEIVED || newStatus == PrintJobStatus.VALIDATED
                || newStatus == PrintJobStatus.QUEUED || newStatus == PrintJobStatus.PROCESSING
                || newStatus == PrintJobStatus.RETRY_SCHEDULED) {
            this.completedAt = null;
        }
        if (newStatus == PrintJobStatus.PROCESSING) this.processingStartedAt = now;
        if (newStatus == PrintJobStatus.SUBMITTED) this.submittedAt = now;
        if (newStatus == PrintJobStatus.PRINTED) { if (this.submittedAt == null) this.submittedAt = now; this.printedAt = now; this.completedAt = now; }
        if (newStatus == PrintJobStatus.FAILED || newStatus == PrintJobStatus.CANCELLED) this.completedAt = now;
        if (newStatus != PrintJobStatus.RETRY_SCHEDULED) this.nextAttemptAt = null;
    }
    public void reserve(String workerId, Instant now) { this.workerId = workerId; transitionTo(PrintJobStatus.PROCESSING, now, null, null); }
    public void scheduleRetry(Instant nextAttemptAt, String code, String message) {
        this.retryCount++; this.nextAttemptAt = nextAttemptAt; this.status = PrintJobStatus.RETRY_SCHEDULED;
        this.errorCode = code; this.errorMessage = message; this.workerId = null;
    }

    void attachEvent(PrintEvent printEvent) {
        printEvents.add(printEvent);
    }

    public UUID getId() {
        return id;
    }

    public PrintRequest getPrintRequest() {
        return printRequest;
    }

    public Printer getPrinter() {
        return printer;
    }

    public PrintJobStatus getStatus() {
        return status;
    }

    public PrintJob getReprintOfJob() {
        return reprintOfJob;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public Instant getPrintedAt() {
        return printedAt;
    }

    public List<PrintEvent> getPrintEvents() {
        return Collections.unmodifiableList(printEvents);
    }

    public long getVersion() {
        return version;
    }
    public int getRetryCount() { return retryCount; }
    public int getMaxRetries() { return maxRetries; }
    public Instant getNextAttemptAt() { return nextAttemptAt; }
    public Instant getProcessingStartedAt() { return processingStartedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public String getWorkerId() { return workerId; }
}
