package com.yazaki.printingservice.api.dto;
import java.time.Instant; import java.util.UUID; import com.yazaki.printingservice.domain.model.*; import jakarta.validation.constraints.*;
public final class PrinterDtos {
 private PrinterDtos() {}
 public record Upsert(@NotBlank @Size(max=64) String code, @NotBlank @Size(max=128) String name,
   @NotNull PrinterType type, @Size(max=128) String location, @Size(max=2000) String technicalConfiguration,
   @NotNull PrinterStatus status, @Size(max=128) String cupsQueueName, Boolean enabled) {}
 public record Patch(@Size(min=1,max=128) String name, PrinterType type, @Size(max=128) String location,
   @Size(max=2000) String technicalConfiguration, PrinterStatus status, @Size(max=128) String cupsQueueName) {}
 public record View(UUID id,String code,String name,PrinterType type,String location,String technicalConfiguration,
   PrinterStatus status,boolean enabled,String cupsQueueName,Instant createdAt,Instant updatedAt) {}
}
