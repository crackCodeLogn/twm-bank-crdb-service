CREATE TABLE IF NOT EXISTS bank (
    NAME            VARCHAR(500) NOT NULL,
    IFSC            VARCHAR(100) NOT NULL,
    CONTACT_NUMBER  VARCHAR(500) NOT NULL,
    BANK_TYPE       VARCHAR(100) NOT NULL,
    IS_ACTIVE       BOOL,

    CRE_TS          TIMESTAMPTZ NOT NULL,

    PRIMARY KEY (IFSC)
);

alter table bank
    add column COUNTRY_CODE VARCHAR(10) NOT NULL DEFAULT 'IN';

alter table bank
    add constraint bank_pkey PRIMARY KEY (ifsc);