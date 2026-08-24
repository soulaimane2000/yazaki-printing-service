package com.yazaki.printingservice.domain.model;

import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.*;

@Entity @Table(name = "print_attempt")
public class PrintAttempt {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "print_job_id") private PrintJob printJob;
    @Column(name = "attempt_number", nullable = false) private int attemptNumber;
    @Column(name = "started_at", nullable = false) private Instant startedAt;
    @Column(name = "completed_at") private Instant completedAt;
    @Enumerated(EnumType.STRING) @Column(length = 32) private PrintAttemptResult result;
    @Column(name = "error_code", length = 64) private String errorCode;
    @Column(name = "error_message", length = 1000) private String errorMessage;
    protected PrintAttempt() {}
    public PrintAttempt(PrintJob job, int number, Instant startedAt) { this.printJob=job; this.attemptNumber=number; this.startedAt=startedAt; }
    public void complete(PrintAttemptResult result, Instant at, String code, String message) { this.result=result; this.completedAt=at; this.errorCode=code; this.errorMessage=message; }
    public UUID getId(){return id;} public PrintJob getPrintJob(){return printJob;} public int getAttemptNumber(){return attemptNumber;}
    public Instant getStartedAt(){return startedAt;} public Instant getCompletedAt(){return completedAt;} public PrintAttemptResult getResult(){return result;}
    public String getErrorCode(){return errorCode;} public String getErrorMessage(){return errorMessage;}
}
