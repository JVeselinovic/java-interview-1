--liquibase formatted sql
--changeset app:create-sample-table
CREATE TABLE sample
(
    id       serial primary key,
    sample_type_id  varchar(40) NULL,
    create_dt       timestamp NULL,
    create_by       varchar(40) NULL,
    mod_dt          timestamp NULL,
    mod_by          varchar(40) NULL,
    collection_date timestamp NULL,
    subject_id numeric NULL
);
--rollback DROP TABLE IF EXISTS sample CASCADE;
