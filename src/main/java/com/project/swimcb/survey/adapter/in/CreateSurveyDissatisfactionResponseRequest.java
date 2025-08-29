package com.project.swimcb.survey.adapter.in;

import com.project.swimcb.survey.domain.CreateSurveyDissatisfactionResponseCommand;
import com.project.swimcb.survey.domain.SurveyDissatisfactionReason;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(description = "불만족 평가 설문 응답 등록 요청")
public record CreateSurveyDissatisfactionResponseRequest(

    @NotNull(message = "피드백은 null 일 수 없습니다.")
    @Size(min = 10, max = 500, message = "피드백은 10자 이상 500자 이하로 입력해주세요.")
    @Schema(description = "피드백", example = "이거 저거 다 고쳐주세요.")
    String feedback,

    @NotNull(message = "불만족 이유 리스트는 null 일 수 없습니다.")
    @Schema(description = "불만족 이유 리스트", example = "SWIMMING_POOL_INACCURATE|SWIMMING_POOL_HARD_TO_SEE|PROGRAM_INFO_INACCURATE|PROGRAM_INFO_HARD_TO_SEE|FONT_SIZE_INAPPROPRIATE|TOUCH_HARD|OPERATION_COMPLEX|SIGNUP_COMPLEX")
    List<SurveyDissatisfactionReason> reasons
) {

  public CreateSurveyDissatisfactionResponseCommand toCommand(
      Long memberId
  ) {
    return CreateSurveyDissatisfactionResponseCommand.builder()
        .memberId(memberId)
        .feedback(feedback)
        .reasons(reasons)
        .build();
  }

}
