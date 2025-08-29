package com.project.swimcb.db.entity;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.project.swimcb.db.entity.enums.SurveyResponseDissatisfactionReasonCode;
import com.project.swimcb.survey.domain.SurveyDissatisfactionReason;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.val;

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

  @Builder(builderMethodName = "create")
  public SurveyResponseDissatisfactionReasonEntity(
      @NonNull SurveyResponseEntity surveyResponseEntity,
      @NonNull SurveyDissatisfactionReason surveyDissatisfactionReason
      ) {

    val surveyResponseDissatisfactionReasonCode = SurveyResponseDissatisfactionReasonCode.valueOf(
        surveyDissatisfactionReason.name());

    this.id = new SurveyResponseDissatisfactionReasonEntityId(
        surveyResponseEntity.getId(),
        surveyResponseDissatisfactionReasonCode
    );
    this.surveyResponseEntity = surveyResponseEntity;
    this.createdAt = LocalDateTime.now();
    this.createdBy = "SYSTEM";
  }

}
