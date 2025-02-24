--liquibase formatted sql

--changeset pue1etproof:create-swimming-class-sub-type-table
--comment: 수영 클래스 강습구분 테이블 생성

create table swimming_class_sub_type
(
    id                     bigserial primary key not null,
    swimming_class_type_id bigint                not null,
    name                   varchar(20)           not null,
    price                  int                   not null,
    created_at             timestamp             not null default now(),
    created_by             varchar(255)          not null,
    updated_at             timestamp             not null default now(),
    updated_by             varchar(255)          not null,
    constraint fk_swimming_class_sub_type_swimming_class_type_id foreign key (swimming_class_type_id) references swimming_class_type (id)
);

--rollback drop table swimming_class_sub_type;