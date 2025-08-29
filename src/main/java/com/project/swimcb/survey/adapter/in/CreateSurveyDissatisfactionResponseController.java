package com.project.swimcb.survey.adapter.in;

import com.project.swimcb.survey.application.port.in.CreateSurveyDissatisfactionResponseUseCase;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "설문지")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/surveys/dissatisfaction-responses")
@RequiredArgsConstructor
public class CreateSurveyDissatisfactionResponseController {

  private final CreateSurveyDissatisfactionResponseUseCase useCase;

  @Operation(summary = "불만족 평가 설문 응답 등록")
  @PostMapping
  public void createSurveyDissatisfactionResponse(
      @Valid @RequestBody CreateSurveyDissatisfactionResponseRequest request,
      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
    useCase.createSurveyDissatisfactionResponse(request.toCommand(tokenInfo.memberId()));
  }

}
