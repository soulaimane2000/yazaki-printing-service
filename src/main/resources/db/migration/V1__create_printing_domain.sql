CREATE TABLE printer (
    id UUID PRIMARY KEY,
    code VARCHAR(64) NOT NULL,
    name VARCHAR(128) NOT NULL,
    type VARCHAR(32) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT uk_printer_code UNIQUE (code),
    CONSTRAINT ck_printer_code_not_blank CHECK (btrim(code) <> ''),
    CONSTRAINT ck_printer_name_not_blank CHECK (btrim(name) <> ''),
    CONSTRAINT ck_printer_type CHECK (type IN ('LABEL', 'DOCUMENT')),
    CONSTRAINT ck_printer_timestamps CHECK (updated_at >= created_at)
);

CREATE INDEX ix_printer_type_enabled ON printer (type, enabled);

CREATE TABLE print_request (
    id UUID PRIMARY KEY,
    request_reference VARCHAR(128) NOT NULL,
    received_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT uk_print_request_reference UNIQUE (request_reference),
    CONSTRAINT ck_print_request_reference_not_blank CHECK (btrim(request_reference) <> '')
);

CREATE INDEX ix_print_request_received_at ON print_request (received_at);

CREATE TABLE print_job (
    id UUID PRIMARY KEY,
    print_request_id UUID NOT NULL,
    printer_id UUID NOT NULL,
    status VARCHAR(32) NOT NULL,
    reprint_of_job_id UUID,
    submitted_at TIMESTAMP WITH TIME ZONE,
    printed_at TIMESTAMP WITH TIME ZONE,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_print_job_request
        FOREIGN KEY (print_request_id) REFERENCES print_request (id),
    CONSTRAINT fk_print_job_printer
        FOREIGN KEY (printer_id) REFERENCES printer (id),
    CONSTRAINT fk_print_job_reprint
        FOREIGN KEY (reprint_of_job_id) REFERENCES print_job (id),
    CONSTRAINT ck_print_job_status CHECK (
        status IN (
            'RECEIVED',
            'VALIDATED',
            'QUEUED',
            'PROCESSING',
            'SUBMITTED',
            'PRINTED',
            'RETRY_SCHEDULED',
            'FAILED',
            'CANCELLED'
        )
    ),
    CONSTRAINT ck_print_job_not_own_reprint CHECK (
        reprint_of_job_id IS NULL OR reprint_of_job_id <> id
    ),
    CONSTRAINT ck_print_job_submitted_at CHECK (
        status NOT IN ('SUBMITTED', 'PRINTED') OR submitted_at IS NOT NULL
    ),
    CONSTRAINT ck_print_job_printed_at_status CHECK (
        printed_at IS NULL OR status = 'PRINTED'
    ),
    CONSTRAINT ck_print_job_printed_at_required CHECK (
        status <> 'PRINTED' OR printed_at IS NOT NULL
    ),
    CONSTRAINT ck_print_job_printed_after_submitted CHECK (
        printed_at IS NULL OR (submitted_at IS NOT NULL AND printed_at >= submitted_at)
    ),
    CONSTRAINT ck_print_job_timestamps CHECK (updated_at >= created_at)
);

CREATE INDEX ix_print_job_request_created ON print_job (print_request_id, created_at);
CREATE INDEX ix_print_job_printer_status ON print_job (printer_id, status);
CREATE INDEX ix_print_job_status_created ON print_job (status, created_at);
CREATE INDEX ix_print_job_reprint ON print_job (reprint_of_job_id)
    WHERE reprint_of_job_id IS NOT NULL;

CREATE TABLE print_event (
    id UUID PRIMARY KEY,
    print_job_id UUID NOT NULL,
    sequence_number BIGINT NOT NULL,
    status VARCHAR(32) NOT NULL,
    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL,
    details VARCHAR(1000),
    CONSTRAINT fk_print_event_job
        FOREIGN KEY (print_job_id) REFERENCES print_job (id),
    CONSTRAINT uk_print_event_job_sequence UNIQUE (print_job_id, sequence_number),
    CONSTRAINT ck_print_event_sequence_positive CHECK (sequence_number > 0),
    CONSTRAINT ck_print_event_status CHECK (
        status IN (
            'RECEIVED',
            'VALIDATED',
            'QUEUED',
            'PROCESSING',
            'SUBMITTED',
            'PRINTED',
            'RETRY_SCHEDULED',
            'FAILED',
            'CANCELLED'
        )
    )
);

CREATE INDEX ix_print_event_job_occurred ON print_event (print_job_id, occurred_at);
