--liquibase formatted sql
--changeset app:create-subject-table
CREATE TABLE subject
(
    id  serial primary key,
    create_dt   timestamp NULL,
    create_by   varchar(40)  NULL,
    mod_dt      timestamp NULL,
    mod_by      varchar(40)  NULL,
    external_id varchar(40) NULL
);
--rollback DROP TABLE IF EXISTS subject CASCADE;
