-- Table: public.tenant

-- DROP TABLE IF EXISTS public.tenant;

CREATE TABLE "tenant"
(
    tenant_id character varying(255) NOT NULL,
    schema character varying(255),
    CONSTRAINT tenant_pkey PRIMARY KEY (tenant_id)
)