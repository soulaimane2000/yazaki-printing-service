export type PrinterType = 'LABEL' | 'DOCUMENT';
export type PrinterStatus = 'ONLINE' | 'OFFLINE' | 'ERROR' | 'UNKNOWN';
export type JobStatus = 'RECEIVED' | 'VALIDATED' | 'QUEUED' | 'PROCESSING' | 'SUBMITTED' | 'PRINTED' | 'RETRY_SCHEDULED' | 'FAILED' | 'CANCELLED';

export interface Page<T> { content:T[]; totalElements:number; totalPages:number; number:number; size:number; first:boolean; last:boolean }
export interface PrintJob { id:string; requestId:string; sourceSystem:string; externalRequestId:string; orderId:string|null; printType:PrinterType; printerId:string; printerName:string; status:JobStatus; retryCount:number; maxRetries:number; nextAttemptAt:string|null; createdAt:string; updatedAt:string; completedAt:string|null; errorCode:string|null; errorMessage:string|null; reprintOfJobId:string|null }
export interface PrintEvent { id:string; sequenceNumber:number; eventType:JobStatus; message:string|null; createdAt:string }
export interface PrintAttempt { id:string; attemptNumber:number; startedAt:string; completedAt:string|null; result:'SUCCESS'|'TEMPORARY_FAILURE'|'PERMANENT_FAILURE'|'TIMEOUT'|'PRINTER_OFFLINE'|null; errorCode:string|null; errorMessage:string|null }
export interface Printer { id:string; code:string; name:string; type:PrinterType; location:string|null; technicalConfiguration:string|null; status:PrinterStatus; enabled:boolean; cupsQueueName:string|null; createdAt:string; updatedAt:string }
export interface DashboardSummary { totalJobs:number; pending:number; submitted:number; printed:number; failed:number; retries:number; onlinePrinters:number; offlinePrinters:number }
export interface StatusCount { status:PrinterStatus; count:number }
export interface DashboardError { jobId:string; errorCode:string|null; errorMessage:string|null; occurredAt:string|null }
export interface JobTrend { day:string; count:number }
export interface Distribution { key:string; count:number }
export interface ApiError { timestamp:string; status:number; code:string; message:string; path:string; fieldErrors:Record<string,string> }
export interface JobFilters { page?:number; size?:number; status?:JobStatus; type?:PrinterType; printerId?:string; orderId?:string; from?:string; to?:string }
export interface CreateJobInput { sourceSystem:string; externalRequestId:string; orderId?:string; printType:PrinterType; payload:string; printerId:string; maxRetries?:number }
export interface PrinterInput { code:string; name:string; type:PrinterType; location?:string; technicalConfiguration?:string; status:PrinterStatus; cupsQueueName?:string; enabled:boolean }
