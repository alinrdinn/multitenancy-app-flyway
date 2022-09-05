-- Table: public.tenant

-- DROP TABLE IF EXISTS public.tenant;

CREATE TABLE "employee"
(
    email character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    verified boolean,
    CONSTRAINT employee_pkey PRIMARY KEY (email)
)