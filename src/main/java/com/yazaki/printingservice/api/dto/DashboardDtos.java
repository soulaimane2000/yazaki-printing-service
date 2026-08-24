package com.yazaki.printingservice.api.dto;
import java.time.*; import java.util.*; import com.yazaki.printingservice.domain.model.*;
public final class DashboardDtos { private DashboardDtos(){}
 public record Summary(long totalJobs,long pending,long submitted,long printed,long failed,long retries,long onlinePrinters,long offlinePrinters){}
 public record PrinterStatusCount(PrinterStatus status,long count){}
 public record ErrorItem(UUID jobId,String errorCode,String errorMessage,Instant occurredAt){}
 public record Trend(LocalDate day,long count){}
 public record Distribution(String key,long count){}
}
