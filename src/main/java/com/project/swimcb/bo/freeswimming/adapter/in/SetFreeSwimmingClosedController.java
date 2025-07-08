package com.project.swimcb.bo.freeswimming.adapter.in;

import com.project.swimcb.bo.freeswimming.application.port.in.SetFreeSwimmingClosedUseCase;
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

@Tag(name = "BO - 완료")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/free-swimming/close")
@RequiredArgsConstructor
public class SetFreeSwimmingClosedController {

  private final SetFreeSwimmingClosedUseCase useCase;

  @Operation(summary = "자유수영 데이터 관리 - 휴무일 설정")
  @PostMapping
  public void setFreeSwimmingClosed(
      @Valid @RequestBody SetFreeSwimmingClosedRequest request,
      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {

    useCase.setFreeSwimmingClosed(request.toCommand(tokenInfo.swimmingPoolId()));
  }

}
