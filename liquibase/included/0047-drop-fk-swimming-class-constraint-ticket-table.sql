--liquibase formatted sql

--changeset pu1etproof:drop-fk-swimming-class-constraint-ticket-table
--comment: 수영 클래스 티켓 테이블의 수영 클래스 외래 키 제약 조건 제거

alter table ticket drop constraint fk_swimming_class_ticket_swimming_class_id;

--rollback alter table ticket add constraint fk_swimming_class_ticket_swimming_class_id foreign key (target_id) references swimming_class (id);
