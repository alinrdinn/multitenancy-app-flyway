CREATE TABLE IF NOT EXISTS "verification_code"
(
    verification_code character(32) NOT NULL,
    tenant_id character varying(255),
    CONSTRAINT verification_code_pkey PRIMARY KEY (verification_code),
    CONSTRAINT tenant_id_fkey FOREIGN KEY (tenant_id)
        REFERENCES tenant (tenant_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)