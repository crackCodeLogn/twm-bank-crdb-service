CREATE TABLE IF NOT EXISTS bank_account
(
    ID                 UUID                  DEFAULT gen_random_uuid(),
    BANK_IFSC          VARCHAR(100) NOT NULL,
    ACCOUNT_NUMBER     VARCHAR(100) NOT NULL,
    ACCOUNT_NAME       VARCHAR(500) NOT NULL,
    TRANSIT_NUMBER     VARCHAR(20)  NOT NULL,
    INSTITUTION_NUMBER VARCHAR(20)  NOT NULL,
    BALANCE            NUMERIC      NOT NULL DEFAULT '0.0',
    BANK_ACCOUNT_TYPES VARCHAR(100) NOT NULL,
    META_DATA          VARCHAR(5000),
    OVERDRAFT_BALANCE  NUMERIC      NOT NULL,
    INTEREST_RATE      NUMERIC      NOT NULL DEFAULT '0.0',
    IS_ACTIVE          BOOL         NOT NULL,
    NOTE               VARCHAR(5000)         DEFAULT '',
    CCY                VARCHAR(10)           DEFAULT 'CAD',
    EXTERNAL_ID        VARCHAR(512)          DEFAULT '',

    CRE_TS             TIMESTAMPTZ  NOT NULL,
    LAST_UPD_TS        TIMESTAMPTZ  NOT NULL,

    PRIMARY KEY (ID),
    FOREIGN KEY (BANK_IFSC) REFERENCES bank (ifsc)
);

-- alter table bank_account
--     add column NOTE VARCHAR(5000) DEFAULT '';
--
-- alter table bank_account
--     add column CCY VARCHAR(10) DEFAULT 'CAD';
--
-- alter table bank_account
--     rename column bank_account_type to bank_account_types;
--
-- alter table bank_account
--     rename column external_id VARCHAR(512) DEFAULT '';
