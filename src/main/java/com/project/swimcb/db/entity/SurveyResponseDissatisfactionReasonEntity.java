package com.project.swimcb.db.entity;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "survey_response_dissatisfaction_reason")
@Entity
@NoArgsConstructor(access = PROTECTED)
public class SurveyResponseDissatisfactionReasonEntity {

  @EmbeddedId
  private SurveyResponseDissatisfactionReasonEntityId id;

  @MapsId("responseId")
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "response_id", nullable = false)
  private SurveyResponseEntity surveyResponseEntity;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "created_by", nullable = false)
  private String createdBy;

}
