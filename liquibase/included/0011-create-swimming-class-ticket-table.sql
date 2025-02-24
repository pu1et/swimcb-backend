--liquibase formatted sql

--changeset pue1etproof:create-swimming-class-ticket-table
--comment: 수영 클래스 티켓 테이블 생성

create table swimming_class_ticket
(
    id                bigserial primary key not null,
    swimming_class_id bigint                not null,
    name              varchar(20)           not null,
    price             int                   not null,
    constraint fk_swimming_class_ticket_swimming_class_id foreign key (swimming_class_id) references swimming_class (id)
);

--rollback drop table swimming_class_ticket;