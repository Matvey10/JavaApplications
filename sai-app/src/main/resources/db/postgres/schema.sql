--liquibase formatted sql logicalFilePath:db/postgres/schema.sql

--changeset vasilev:initial-schema
--preconditions onFail:MARK_RAN onError:HALT onUpdateSql:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'ARTICLE';
create SEQUENCE ai_area_seq START with 1;
create SEQUENCE article_seq START with 1;
create SEQUENCE author_seq START with 1;
create SEQUENCE development_seq START with 1;
create SEQUENCE development_type_seq START with 1;
create SEQUENCE keyword_seq START with 1;
create SEQUENCE method_seq START with 1;
create SEQUENCE sai_property_seq START with 1;
create SEQUENCE technology_seq START with 1;

create table ARTICLE (
    id              NUMERIC(22) not null primary key,
    article_title   VARCHAR(200) not null,
    magazine        VARCHAR(100) not null,
    issue_number    NUMERIC(10) not null,
    year            NUMERIC(4) not null
);

create table AUTHOR (
    id              NUMERIC(22) not null primary key,
    surname         VARCHAR(30) not null,
    name            VARCHAR(30) not null,
    patronymic      VARCHAR(30),
    sex             VARCHAR(1) not null,
    country         VARCHAR(30)
);

create table ARTICLE_AUTHOR (
    article_id      NUMERIC(22) REFERENCES ARTICLE (id),
    author_id       NUMERIC(22) REFERENCES AUTHOR (id),
    CONSTRAINT author_article_pkey PRIMARY KEY (article_id, author_id)
);

create table KEYWORD (
    id              NUMERIC(22) not null primary key,
    value           VARCHAR(100) not null
);

create table ARTICLE_KEYWORD (
    article_id      NUMERIC(22) REFERENCES ARTICLE (id),
    keyword_id      NUMERIC(22) REFERENCES KEYWORD (id),
    CONSTRAINT article_keyword_pkey PRIMARY KEY (article_id, keyword_id)
);

create table DEVELOPMENT_TYPE (
    id                    NUMERIC(22) not null primary key,
    type_name             VARCHAR(100) not null
);

create table DEVELOPMENT (
    id                    NUMERIC(22) not null primary key,
    development_name      VARCHAR(100) not null,
    year                  NUMERIC(4) not null,
    development_type_id   NUMERIC(22) REFERENCES DEVELOPMENT_TYPE (id)
);

create table ARTICLE_DEVELOPMENT (
    article_id            NUMERIC(22) REFERENCES ARTICLE (id),
    development_id        NUMERIC(22) REFERENCES DEVELOPMENT (id),
    CONSTRAINT article_development_pkey PRIMARY KEY (article_id, development_id)
);

create table AUTHOR_DEVELOPMENT(
    author_id            NUMERIC(22) REFERENCES AUTHOR (id),
    development_id        NUMERIC(22) REFERENCES DEVELOPMENT (id),
    CONSTRAINT author_development_pkey PRIMARY KEY (author_id, development_id)
);

create table AI_AREA (
    id                    NUMERIC(22) not null primary key,
    area_name             VARCHAR(100) not null,
    area_branch           VARCHAR(100) not null
);

create table ARTICLE_AI_AREA (
    article_id            NUMERIC(22) REFERENCES ARTICLE (id),
    ai_area_id            NUMERIC(22) REFERENCES AI_AREA (id),
    CONSTRAINT article_ai_area_pkey PRIMARY KEY (article_id, ai_area_id)
);

create table METHOD (
    id                    NUMERIC(22) not null primary key,
    method_name           VARCHAR(100) not null,
    science_branch        VARCHAR(100) not null
);

create table AI_AREA_METHOD (
    ai_area_id            NUMERIC(22) REFERENCES AI_AREA (id),
    method_id             NUMERIC(22) REFERENCES METHOD (id),
    CONSTRAINT ai_area_method_pkey PRIMARY KEY (ai_area_id, method_id)
);

create table SAI_PROPERTY (
    id                    NUMERIC(22) not null primary key,
    property_name         VARCHAR(100) not null
);

create table DEVELOPMENT_SAI_PROPERTY (
    development_id        NUMERIC(22) REFERENCES DEVELOPMENT (id),
    sai_property_id       NUMERIC(22) REFERENCES SAI_PROPERTY (id),
    CONSTRAINT development_sai_property_pkey PRIMARY KEY (development_id, sai_property_id)
);

create table TECHNOLOGY (
    id                    NUMERIC(22) not null primary key,
    technology_name       VARCHAR(100) not null
);

create table DEVELOPMENT_TECHNOLOGY (
    development_id        NUMERIC(22) REFERENCES DEVELOPMENT (id),
    technology_id         NUMERIC(22) REFERENCES TECHNOLOGY (id),
    CONSTRAINT development_technology_pkey PRIMARY KEY (development_id, technology_id)
);
--comment initial-schema
--rollback not required


