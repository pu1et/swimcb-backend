package com.project.swimcb.survey.adapter.in;

import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "설문지")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/surveys/me/completed")
public class CheckMySurveyCompletedController {

  @Operation(summary = "사용자 설문 여부 조회")
  @GetMapping
  public CheckMySurveyCompletedResponse checkMySurveyCompleted(
      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
    return null;
  }

}
