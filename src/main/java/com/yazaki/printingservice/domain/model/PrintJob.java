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
        printRequest.attachJob(this);
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
}
