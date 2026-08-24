package com.yazaki.printingservice.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "print_request")
public class PrintRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(max = 256)
    @Column(name = "request_reference", nullable = false, unique = true, length = 256)
    private String requestReference;

    @Column(name = "external_request_id", nullable = false, length = 128)
    private String externalRequestId;

    @Column(name = "source_system", nullable = false, length = 64)
    private String sourceSystem;

    @Column(name = "order_id", length = 128)
    private String orderId;

    @Enumerated(jakarta.persistence.EnumType.STRING)
    @Column(name = "print_type", nullable = false, length = 32)
    private PrinterType printType;

    @Column(nullable = false, columnDefinition = "text")
    private String payload;

    @Column(name = "payload_hash", nullable = false, length = 64)
    private String payloadHash;

    @NotNull
    @Column(name = "received_at", nullable = false, updatable = false)
    private Instant receivedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "printRequest")
    private List<PrintJob> printJobs = new ArrayList<>();

    protected PrintRequest() {
    }

    public PrintRequest(String requestReference, Instant receivedAt) {
        this.requestReference = requestReference;
        this.externalRequestId = requestReference;
        this.sourceSystem = "LEGACY";
        this.printType = PrinterType.LABEL;
        this.payload = "{}";
        this.payloadHash = "0".repeat(64);
        this.receivedAt = receivedAt;
    }

    public PrintRequest(String externalRequestId, String sourceSystem, String orderId, PrinterType printType,
            String payload, String payloadHash, Instant receivedAt) {
        this.requestReference = sourceSystem + ":" + externalRequestId;
        this.externalRequestId = externalRequestId;
        this.sourceSystem = sourceSystem;
        this.orderId = orderId;
        this.printType = printType;
        this.payload = payload;
        this.payloadHash = payloadHash;
        this.receivedAt = receivedAt;
    }

    @jakarta.persistence.PrePersist
    void initializeCreatedAt() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    void attachJob(PrintJob printJob) {
        printJobs.add(printJob);
    }

    public UUID getId() {
        return id;
    }

    public String getRequestReference() {
        return requestReference;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getExternalRequestId() { return externalRequestId; }
    public String getSourceSystem() { return sourceSystem; }
    public String getOrderId() { return orderId; }
    public PrinterType getPrintType() { return printType; }
    public String getPayload() { return payload; }
    public String getPayloadHash() { return payloadHash; }

    public List<PrintJob> getPrintJobs() {
        return Collections.unmodifiableList(printJobs);
    }
}
