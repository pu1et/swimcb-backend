--liquibase formatted sql

--changeset pu1etproof:create-free-swimming-table:0045
--comment: 자유수영 일별 상태 테이블 생성

create table free_swimming_day_status
(
    id                     bigserial primary key not null,
    free_swimming_id       bigint                not null,
    day_of_month           int                   not null,
    reserved_count         int                   not null,
    is_closed              boolean               not null,
    is_reservation_blocked boolean               not null,
    created_at             timestamp             not null default now(),
    created_by             varchar(255)          not null,
    updated_at             timestamp             not null default now(),
    updated_by             varchar(255)          not null
);

--rollback drop table free_swimming_day_status;
