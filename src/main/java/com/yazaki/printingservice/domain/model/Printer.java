package com.yazaki.printingservice.domain.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "printer")
public class Printer extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(max = 64)
    @Column(nullable = false, unique = true, length = 64)
    private String code;

    @NotBlank
    @Size(max = 128)
    @Column(nullable = false, length = 128)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private PrinterType type;

    @Size(max = 128)
    @Column(length = 128)
    private String location;

    @Size(max = 2000)
    @Column(name = "technical_configuration", length = 2000)
    private String technicalConfiguration;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private PrinterStatus status;

    @Size(max = 128)
    @Column(name = "cups_queue_name", length = 128)
    private String cupsQueueName;

    @Column(nullable = false)
    private boolean enabled;

    @Version
    @Column(nullable = false)
    private long version;

    protected Printer() {
    }

    public Printer(String code, String name, PrinterType type) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.status = PrinterStatus.UNKNOWN;
        this.enabled = true;
    }

    public void update(String name, PrinterType type, String location, String technicalConfiguration,
            PrinterStatus status, String cupsQueueName) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.technicalConfiguration = technicalConfiguration;
        this.status = status;
        this.cupsQueueName = cupsQueueName;
    }

    public void activate() { this.enabled = true; }
    public void deactivate() { this.enabled = false; }

    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public PrinterType getType() {
        return type;
    }

    public String getLocation() { return location; }
    public String getTechnicalConfiguration() { return technicalConfiguration; }
    public PrinterStatus getStatus() { return status; }
    public String getCupsQueueName() { return cupsQueueName; }

    public boolean isEnabled() {
        return enabled;
    }

    public long getVersion() {
        return version;
    }
}
