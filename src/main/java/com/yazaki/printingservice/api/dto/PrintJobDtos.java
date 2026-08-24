package com.yazaki.printingservice.api.dto;

import java.time.Instant; import java.util.UUID;
import com.yazaki.printingservice.domain.model.*;
import jakarta.validation.constraints.*;

public final class PrintJobDtos {
 private PrintJobDtos() {}
 public record Create(@NotBlank @Size(max=64) String sourceSystem,
   @NotBlank @Size(max=128) String externalRequestId, @Size(max=128) String orderId,
   @NotNull PrinterType printType, @NotBlank @Size(max=100000) String payload,
   @NotNull UUID printerId, @Min(0) @Max(20) Integer maxRetries) {}
 public record Job(UUID id, UUID requestId, String sourceSystem, String externalRequestId, String orderId,
   PrinterType printType, UUID printerId, String printerName, PrintJobStatus status, int retryCount,
   int maxRetries, Instant nextAttemptAt, Instant createdAt, Instant updatedAt, Instant completedAt,
   String errorCode, String errorMessage, UUID reprintOfJobId) {}
 public record Event(UUID id, long sequenceNumber, PrintJobStatus eventType, String message, Instant createdAt) {}
}
