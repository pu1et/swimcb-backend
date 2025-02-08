--liquibase formatted sql

--changeset pue1etproof:create-notice-table
--comment: 공지사항 테이블 생성

create table notice
(
    id         bigserial primary key not null,
    title      varchar(255)          not null,
    content    text                  not null,
    is_visible boolean               not null,
    created_at timestamp             not null default now(),
    created_by varchar(255)          not null,
    updated_at timestamp             not null default now(),
    updated_by varchar(255)          not null
);

--rollback drop table notice;