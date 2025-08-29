package com.project.swimcb.db.entity;

import static com.project.swimcb.db.entity.enums.SurveyResponseType.DISSATISFACTION;
import static com.project.swimcb.db.entity.enums.SurveyResponseType.SATISFACTION;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.project.swimcb.db.entity.enums.SurveyResponseType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@Table(name = "survey_response")
@Entity
@NoArgsConstructor(access = PROTECTED)
public class SurveyResponseEntity {

  @GeneratedValue(strategy = IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @OneToOne(fetch = LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private MemberEntity member;

  @Enumerated(STRING)
  @Column(name = "response_type", nullable = false)
  private SurveyResponseType type;

  @Column(name = "overall_rating")
  private Integer overallRating;

  @Column(name = "find_pool_rating")
  private Integer findPoolRating;

  @Column(name = "reservation_rating")
  private Integer reservationRating;

  @Column(name = "usability_rating")
  private Integer usabilityRating;

  @Column(name = "satisfaction_feedback", length = 500)
  private String satisfactionFeedback;

  @Column(name = "dissatisfaction_feedback", length = 500)
  private String dissatisfactionFeedback;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "created_by", nullable = false)
  private String createdBy;

  @Builder(builderClassName = "SatisfactionBuilder", builderMethodName = "createSatisfaction")
  public SurveyResponseEntity(
      MemberEntity member,
      @NonNull Integer overallRating,
      @NonNull Integer findPoolRating,
      @NonNull Integer reservationRating,
      @NonNull Integer usabilityRating,
      @NonNull String satisfactionFeedback
  ) {
    this.member = member;
    this.type = SATISFACTION;
    this.overallRating = overallRating;
    this.findPoolRating = findPoolRating;
    this.reservationRating = reservationRating;
    this.usabilityRating = usabilityRating;
    this.satisfactionFeedback = satisfactionFeedback;
    this.createdAt = LocalDateTime.now();
    this.createdBy = "SYSTEM";
  }

  @Builder(builderClassName = "DissatisfactionBuilder", builderMethodName = "createDissatisfaction")
  public SurveyResponseEntity(
      MemberEntity member,
      @NonNull String dissatisfactionFeedback
  ) {
    this.member = member;
    this.type = DISSATISFACTION;
    this.dissatisfactionFeedback = dissatisfactionFeedback;
    this.createdAt = LocalDateTime.now();
    this.createdBy = "SYSTEM";
  }

}
