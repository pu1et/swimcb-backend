package com.project.swimcb.survey.adapter.in;

import com.project.swimcb.survey.application.port.in.CreateSurveySatisfactionResponseUseCase;
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
@RequestMapping("/api/surveys/satisfaction-responses")
@RequiredArgsConstructor
public class CreateSurveySatisfactionResponseController {

  private final CreateSurveySatisfactionResponseUseCase useCase;

  @Operation(summary = "만족도 평가 설문 응답 등록")
  @PostMapping
  public void createSurveySatisfactionResponse(
      @Valid @RequestBody CreateSurveySatisfactionResponseRequest request,
      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
    useCase.createSurveySatisfactionResponse(request.toCommand(tokenInfo.memberId()));
  }

}
