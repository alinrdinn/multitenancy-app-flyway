CREATE TABLE IF NOT EXISTS "verification_code"
(
    verification_code character(32) NOT NULL,
    employee_email character varying(255),
    CONSTRAINT verification_code_pkey PRIMARY KEY (verification_code),
    CONSTRAINT email_fkey FOREIGN KEY (email)
        REFERENCES employee (email) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)