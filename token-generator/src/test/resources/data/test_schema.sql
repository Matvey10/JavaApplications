CREATE TYPE BYTEA AS BLOB;

CREATE TABLE bpcmerchantdetails
(
    id                             BIGINT PRIMARY KEY                  NOT NULL,
    merchantLogin                  VARCHAR(100),
);

CREATE TABLE merchant_currency (
    id           BIGINT PRIMARY KEY,
    merchant_id  BIGINT NOT NULL,
    numeric_code CHAR(3),
    is_default   BOOLEAN DEFAULT FALSE,
    CONSTRAINT unique_merchant_currency UNIQUE (merchant_id, numeric_code)
);
CREATE SEQUENCE merchant_currency_id_seq;

CREATE TABLE vendor_pay (
    id            BIGINT                  NOT NULL PRIMARY KEY,
    merchant_id   BIGINT                  NOT NULL,
    vendor        VARCHAR(20)             NOT NULL,
    enabled       BOOLEAN                 NOT NULL,
    creation_date TIMESTAMP               NOT NULL
);
CREATE SEQUENCE SEQ_VENDORPAY;

CREATE TABLE apple_pay (
    id                BIGINT        NOT NULL PRIMARY KEY,
    public_key        VARCHAR(4000) NOT NULL,
    private_key       VARCHAR(4000) NOT NULL,
    csr               VARCHAR(4000) NOT NULL,
    algorithm         VARCHAR(20)   NOT NULL,
    merchant_apple_id VARCHAR(255),
    public_key_hash   VARCHAR(255)
);

CREATE TABLE samsung_pay (
    id                BIGINT        NOT NULL PRIMARY KEY,
    public_key        VARCHAR(4000) NOT NULL,
    private_key       VARCHAR(4000) NOT NULL,
    csr               VARCHAR(4000) NOT NULL,
);






