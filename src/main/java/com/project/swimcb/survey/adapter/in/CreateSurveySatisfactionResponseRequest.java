package com.project.swimcb.survey.adapter.in;

import com.project.swimcb.survey.domain.CreateSurveySatisfactionResponseCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "만족도 평가 설문 응답 등록 요청")
public record CreateSurveySatisfactionResponseRequest(

    @Schema(description = "전체적인 만족도 (1.0 ~ 5.0 사이의 값)", example = "4.5")
    @NotNull(message = "전체적인 만족도는 null일 수 없습니다")
    @Min(value = 1, message = "전체적인 만족도는 1 이상이어야 합니다")
    @Max(value = 5, message = "전체적인 만족도는 5 이하여야 합니다")
    Integer overallRating,

    @Schema(description = "수영장 찾기 용이함 점수 (1 ~ 3 사이의 값)", example = "2")
    @NotNull(message = "수영장 찾기 용이함 점수는 null일 수 없습니다")
    @Min(value = 1, message = "수영장 찾기 용이함 점수는 1 이상이어야 합니다")
    @Max(value = 3, message = "수영장 찾기 용이함 점수는 3 이하여야 합니다")
    Integer findPoolRating,

    @Schema(description = "예약 과정 간편 점수 (1 ~ 3 사이의 값)", example = "3")
    @NotNull(message = "예약 과정 간편 점수는 null일 수 없습니다")
    @Min(value = 1, message = "예약 과정 간편 점수는 1 이상이어야 합니다")
    @Max(value = 3, message = "예약 과정 간편 점수는 3 이하여야 합니다")
    Integer reservationRating,

    @Schema(description = "조작법 직관적 점수 (1 ~ 3 사이의 값)", example = "1")
    @NotNull(message = "조작법 직관적 점수는 null일 수 없습니다")
    @Min(value = 1, message = "조작법 직관적 점수는 1 이상이어야 합니다")
    @Max(value = 3, message = "조작법 직관적 점수는 3 이하여야 합니다")
    Integer usabilityRating,

    @Schema(description = "추가 의견", example = "더 많은 수영 강습 프로그램이 있었으면 좋겠습니다.")
    @NotNull(message = "추가 의견은 null일 수 없습니다")
    @Size(min = 10, max = 500, message = "추가 의견은 10자 이상 500자 이하여야 합니다")
    String feedback
) {

  public CreateSurveySatisfactionResponseCommand toCommand(Long memberId) {
    return CreateSurveySatisfactionResponseCommand.builder()
        .memberId(memberId)
        .overallRating(overallRating)
        .findPoolRating(findPoolRating)
        .reservationRating(reservationRating)
        .usabilityRating(usabilityRating)
        .feedback(feedback)
        .build();
  }

}
