package com.yazaki.printingservice.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @Size(max = 128)
    @Column(name = "request_reference", nullable = false, unique = true, length = 128)
    private String requestReference;

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

    public List<PrintJob> getPrintJobs() {
        return Collections.unmodifiableList(printJobs);
    }
}
