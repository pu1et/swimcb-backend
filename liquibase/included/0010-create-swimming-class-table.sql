--liquibase formatted sql

--changeset pue1etproof:create-swimming-class-table
--comment: 수영 클래스 테이블 생성

create table swimming_class
(
    id                         bigserial primary key not null,
    swimming_pool_id           bigint                not null,
    month                      int                   not null,
    swimming_class_type_id     bigint                not null,
    swimming_class_sub_type_id bigint                not null,
    is_monday                  boolean               not null,
    is_tuesday                 boolean               not null,
    is_wednesday               boolean               not null,
    is_thursday                boolean               not null,
    is_friday                  boolean               not null,
    is_saturday                boolean               not null,
    is_sunday                  boolean               not null,
    start_time                 time(0) without time zone                  not null,
    end_time                   time(0) without time zone                  not null,
    swimming_instructor_id     bigint                not null,
    total_capacity             int                   not null,
    reservation_limit_count    int                   not null,
    is_visible                 boolean               not null,
    created_at                 timestamp             not null default now(),
    created_by                 varchar(255)          not null,
    updated_at                 timestamp             not null default now(),
    updated_by                 varchar(255)          not null,
    constraint fk_swimming_class_swimming_pool_id foreign key (swimming_pool_id) references swimming_pool (id),
    constraint fk_swimming_class_swimming_class_type_id foreign key (swimming_class_type_id) references swimming_class_type (id),
    constraint fk_swimming_class_swimming_class_sub_type_id foreign key (swimming_class_sub_type_id) references swimming_class_sub_type (id),
    constraint fk_swimming_class_swimming_instructor_id foreign key (swimming_instructor_id) references swimming_instructor (id)
);

--rollback drop table swimming_class;