--liquibase formatted sql

--changeset pue1etproof:create-member-table
--comment: MEMBER 테이블 생성

create table member
(
    id         bigserial primary key not null,
    name       varchar(100)          not null,
    birth_date date                  not null,
    phone      varchar(20)           not null,
    gender     varchar(10)           not null,
    email      varchar(255),
    nickname   varchar(100),
    created_at timestamp             not null default now(),
    created_by varchar(255)          not null,
    updated_at timestamp             not null default now(),
    updated_by varchar(255)          not null
);

--rollback drop table member;