package com.project.swimcb.bo.freeswimming.adapter.in;

import com.project.swimcb.bo.freeswimming.application.port.in.UpdateBoFreeSwimmingUseCase;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO - 완료")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/free-swimming/{freeSwimmingId}")
@RequiredArgsConstructor
public class UpdateBoFreeSwimmingController {

  private final UpdateBoFreeSwimmingUseCase useCase;

  @Operation(summary = "자유수영 데이터 관리 - 자유수영 수정")
  @PutMapping
  public void updateFreeSwimmingImage(
      @PathVariable(value = "freeSwimmingId") Long freeSwimmingId,
      @AuthenticationPrincipal TokenInfo tokenInfo,
      @Valid @RequestBody UpdateBoFreeSwimmingRequest request
  ) {
    useCase.updateFreeSwimmingImage(request.toCommand(tokenInfo.swimmingPoolId(), freeSwimmingId));
  }

}
