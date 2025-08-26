package com.project.swimcb.survey.adapter.in;

import com.project.swimcb.survey.domain.SurveyDissatisfactionReason;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "불만족 평가 설문 응답 등록 요청")
public record CreateSurveyDissatisfactionResponseRequest(

    @NotNull(message = "만족도 평가는 null 일 수 없습니다.")
    List<SurveyDissatisfactionReason> reasons
) {

}
