package com.yazaki.printingservice.domain.model;

import java.time.Instant;
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
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "print_event")
public class PrintEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "print_job_id", nullable = false)
    private PrintJob printJob;

    @Positive
    @Column(name = "sequence_number", nullable = false)
    private long sequenceNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private PrintJobStatus status;

    @NotNull
    @Column(name = "occurred_at", nullable = false, updatable = false)
    private Instant occurredAt;

    @Size(max = 1000)
    @Column(length = 1000)
    private String details;

    protected PrintEvent() {
    }

    public PrintEvent(
            PrintJob printJob,
            long sequenceNumber,
            PrintJobStatus status,
            Instant occurredAt,
            String details) {
        this.printJob = printJob;
        this.sequenceNumber = sequenceNumber;
        this.status = status;
        this.occurredAt = occurredAt;
        this.details = details;
        printJob.attachEvent(this);
    }

    public UUID getId() {
        return id;
    }

    public PrintJob getPrintJob() {
        return printJob;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public PrintJobStatus getStatus() {
        return status;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public String getDetails() {
        return details;
    }
}
