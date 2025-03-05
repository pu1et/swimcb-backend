package com.project.swimcb.bo.swimmingpool.adapter.in;

import com.project.swimcb.bo.swimmingpool.application.in.UpdateSwimmingPoolBasicInfoUseCase;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO - 완료")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/swimming-pools/basic-info")
@RequiredArgsConstructor
public class UpdateSwimmingPoolBasicInfoController {

  private final UpdateSwimmingPoolBasicInfoUseCase useCase;

  @Operation(summary = "수영장 기본 정보 수정")
  @PutMapping
  public void updateBasicInfo(
      @AuthenticationPrincipal TokenInfo tokenInfo,
      @Valid @RequestBody UpdateSwimmingPoolBasicInfoRequest request
  ) {
    useCase.updateBasicInfo(tokenInfo.swimmingPoolId(), request.toCommand());
  }
}
