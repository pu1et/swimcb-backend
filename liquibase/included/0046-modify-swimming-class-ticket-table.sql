--liquibase formatted sql

--changeset pu1etproof:modify-swimming-class-ticket-table
--comment: 수영 클래스 티켓 테이블을 자유수영도 포함하도록 수정

alter table swimming_class_ticket rename column swimming_class_id to target_id;
alter table swimming_class_ticket add column target_type varchar(20) not null default 'SWIMMING_CLASS';
alter table swimming_class_ticket rename to ticket;

--rollback alter table ticket rename to swimming_class_ticket;
--rollback alter table swimming_class_ticket drop column target_type;
--rollback alter table swimming_class_ticket rename column target_id to swimming_class_id;
