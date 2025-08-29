package com.project.swimcb.db.entity;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

import com.project.swimcb.db.entity.enums.SurveyResponseDissatisfactionReasonCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@EqualsAndHashCode
public class SurveyResponseDissatisfactionReasonEntityId {

  @Column(name = "response_id", nullable = false)
  private Long responseId;

  @Enumerated(STRING)
  @Column(name = "reason_code", nullable = false)
  private SurveyResponseDissatisfactionReasonCode reasonCode;

}
