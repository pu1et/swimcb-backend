--liquibase formatted sql

--changeset pue1etproof:create-member-table
--comment: 관리자 테이블 생성

create table admin
(
    id         bigserial primary key not null,
    name       varchar(100)          not null,
    phone      varchar(20)           not null,
    login_id   varchar(100)          not null,
    password   varchar(40)           not null,
    created_at timestamp             not null default now(),
    created_by varchar(255)          not null,
    updated_at timestamp             not null default now(),
    updated_by varchar(255)          not null
);

--rollback drop table admin;