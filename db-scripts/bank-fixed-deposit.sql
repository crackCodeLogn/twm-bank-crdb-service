CREATE TABLE IF NOT EXISTS bank_fd (
    FD_NUMBER      VARCHAR(100) NOT NULL,
    USER_FD        VARCHAR(50) NOT NULL,
    CUST_ID        VARCHAR(20) NOT NULL,
    IFSC_BANK      VARCHAR(100) NOT NULL,
    AMT_DPT        FLOAT NOT NULL,
    ROI            FLOAT NOT NULL,
    DT_START       DATE NOT NULL,
    DT_END         DATE NOT NULL,
    MONTHS         INTEGER NOT NULL,
    DAYS           INTEGER NOT NULL,
    TYPE_INTEREST  VARCHAR(50) NOT NULL,
    NOMINEE        VARCHAR(50) NOT NULL,
    AMT_EXP        FLOAT,
    INT_EXP        FLOAT,
    ORIG_USER_FD   VARCHAR(50) NOT NULL,
    IS_ACTIVE      BOOL,

    CRE_TS          TIMESTAMPTZ NOT NULL,

    PRIMARY KEY (FD_NUMBER)
);

ALTER TABLE bank_fd
    ADD column TYPE_ACC INT DEFAULT 0 not null; -- add another column for account typ => 0: IND, 1: TFSA, 2: NR

ALTER TABLE bank_fd
    ADD column FREEZE INT DEFAULT 0 not null; -- add another column for freeze => 0 for no freeze, 1 for freezing total amount