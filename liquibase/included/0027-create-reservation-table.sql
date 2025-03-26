--liquibase formatted sql

--changeset pu1etproof:create-reservation-table
--comment: 예약 테이블 생성

create table reservation
(
    id                 bigserial primary key not null,
    member_id          bigint                not null,
    ticket_type        varchar(20)           not null,
    ticket_id          bigint                not null,
    payment_method     varchar(20)           not null,
    reserved_at        timestamp             not null default now(),
    reservation_status varchar(20)           not null,
    created_at         timestamp             not null default now(),
    created_by         varchar(255)          not null,
    updated_at         timestamp             not null default now(),
    updated_by         varchar(255)          not null,
    constraint fk_reservation_member_id foreign key (member_id) references member (id)
);

--rollback drop table reservation;