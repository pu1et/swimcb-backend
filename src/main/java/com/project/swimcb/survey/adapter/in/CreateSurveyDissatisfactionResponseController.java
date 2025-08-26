package com.project.swimcb.survey.adapter.in;

import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "설문지")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/surveys/dissatisfaction-responses")
public class CreateSurveyDissatisfactionResponseController {

  @Operation(summary = "불만족 평가 설문 응답 등록")
  @PostMapping
  public void createSurveyDissatisfactionResponse(
      @Valid @RequestBody CreateSurveyDissatisfactionResponseRequest request,
      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {

  }

}
