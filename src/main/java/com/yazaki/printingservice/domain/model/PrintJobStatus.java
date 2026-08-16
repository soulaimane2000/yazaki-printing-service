package com.yazaki.printingservice.domain.model;

public enum PrintJobStatus {
    RECEIVED,
    VALIDATED,
    QUEUED,
    PROCESSING,
    SUBMITTED,
    PRINTED,
    RETRY_SCHEDULED,
    FAILED,
    CANCELLED
}
