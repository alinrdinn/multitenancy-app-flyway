CREATE TABLE "tenant"
(
    tenant_id character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    verified boolean,
    CONSTRAINT tenant_pkey PRIMARY KEY (tenant_id)
)