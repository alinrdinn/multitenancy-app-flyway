-- Table: public.tenant

-- DROP TABLE IF EXISTS public.tenant;

CREATE TABLE "tenant"
(
    tenant_id character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    CONSTRAINT tenant_pkey PRIMARY KEY (tenant_id)
)