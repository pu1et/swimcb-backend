--liquibase formatted sql

--changeset pu1etproof:0049-create-survey-response-table
--comment: 설문지 응답 테이블 생성

create table survey_response
(
  id                       bigserial primary key not null,
  member_id                bigint,
  response_type            varchar(20)           not null,
  overall_rating           numeric(2, 1),
  find_pool_rating         smallint,
  reservation_rating       smallint,
  usability_rating         smallint,
  satisfaction_feedback    varchar(500),
  dissatisfaction_feedback varchar(500),
  created_at               timestamp             not null default current_timestamp,
  created_by               varchar(255)          not null,
  constraint fk_survey_response_member_id foreign key (member_id) references member (id)
);

create table survey_response_dissatisfaction_reason
(
  response_id bigint       not null,
  reason_code varchar(255) not null,
  created_at  timestamp    not null default current_timestamp,
  created_by  varchar(255) not null,
  primary key (response_id, reason_code),
  constraint fk_survey_response_dissatisfaction_reason_response_id foreign key (response_id) references survey_response (id)
);

-- rollback drop table survey_response_dissatisfaction_reason;
-- rollback drop table survey_response;
