ALTER TABLE printer
    ADD COLUMN location VARCHAR(128),
    ADD COLUMN technical_configuration VARCHAR(2000),
    ADD COLUMN status VARCHAR(32) NOT NULL DEFAULT 'UNKNOWN',
    ADD COLUMN cups_queue_name VARCHAR(128),
    ADD CONSTRAINT ck_printer_status CHECK (status IN ('ONLINE', 'OFFLINE', 'ERROR', 'UNKNOWN'));
CREATE INDEX ix_printer_status_enabled ON printer (status, enabled);

ALTER TABLE print_request
    ALTER COLUMN request_reference TYPE VARCHAR(256),
    ADD COLUMN external_request_id VARCHAR(128),
    ADD COLUMN source_system VARCHAR(64),
    ADD COLUMN order_id VARCHAR(128),
    ADD COLUMN print_type VARCHAR(32),
    ADD COLUMN payload TEXT,
    ADD COLUMN payload_hash VARCHAR(64);
UPDATE print_request SET external_request_id = request_reference, source_system = 'LEGACY',
    print_type = 'LABEL', payload = '{}', payload_hash = repeat('0', 64);
ALTER TABLE print_request
    ALTER COLUMN external_request_id SET NOT NULL,
    ALTER COLUMN source_system SET NOT NULL,
    ALTER COLUMN print_type SET NOT NULL,
    ALTER COLUMN payload SET NOT NULL,
    ALTER COLUMN payload_hash SET NOT NULL,
    ADD CONSTRAINT uk_print_request_source_external UNIQUE (source_system, external_request_id),
    ADD CONSTRAINT ck_print_request_type CHECK (print_type IN ('LABEL', 'DOCUMENT')),
    ADD CONSTRAINT ck_print_request_hash CHECK (payload_hash ~ '^[0-9a-f]{64}$');
CREATE INDEX ix_print_request_order_id ON print_request (order_id) WHERE order_id IS NOT NULL;

ALTER TABLE print_job
    ADD COLUMN retry_count INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN max_retries INTEGER NOT NULL DEFAULT 3,
    ADD COLUMN next_attempt_at TIMESTAMP WITH TIME ZONE,
    ADD COLUMN processing_started_at TIMESTAMP WITH TIME ZONE,
    ADD COLUMN completed_at TIMESTAMP WITH TIME ZONE,
    ADD COLUMN error_code VARCHAR(64),
    ADD COLUMN error_message VARCHAR(1000),
    ADD COLUMN worker_id VARCHAR(128),
    ADD CONSTRAINT ck_print_job_retry_count CHECK (retry_count >= 0 AND max_retries >= 0 AND retry_count <= max_retries),
    ADD CONSTRAINT ck_print_job_next_attempt CHECK (status <> 'RETRY_SCHEDULED' OR next_attempt_at IS NOT NULL);
CREATE INDEX ix_print_job_queue ON print_job (status, next_attempt_at, created_at)
    WHERE status IN ('QUEUED', 'RETRY_SCHEDULED');
CREATE INDEX ix_print_job_processing ON print_job (processing_started_at)
    WHERE status = 'PROCESSING';

CREATE TABLE print_attempt (
    id UUID PRIMARY KEY,
    print_job_id UUID NOT NULL REFERENCES print_job(id),
    attempt_number INTEGER NOT NULL,
    started_at TIMESTAMP WITH TIME ZONE NOT NULL,
    completed_at TIMESTAMP WITH TIME ZONE,
    result VARCHAR(32),
    error_code VARCHAR(64),
    error_message VARCHAR(1000),
    CONSTRAINT uk_print_attempt_job_number UNIQUE (print_job_id, attempt_number),
    CONSTRAINT ck_print_attempt_number CHECK (attempt_number > 0),
    CONSTRAINT ck_print_attempt_result CHECK (result IS NULL OR result IN ('SUCCESS','TEMPORARY_FAILURE','PERMANENT_FAILURE','TIMEOUT','PRINTER_OFFLINE')),
    CONSTRAINT ck_print_attempt_completed CHECK (completed_at IS NULL OR completed_at >= started_at)
);
CREATE INDEX ix_print_attempt_job_started ON print_attempt (print_job_id, started_at);
