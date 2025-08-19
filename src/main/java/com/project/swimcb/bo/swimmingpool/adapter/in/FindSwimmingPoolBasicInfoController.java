package com.project.swimcb.bo.swimmingpool.adapter.in;

import com.project.swimcb.bo.swimmingpool.application.in.FindSwimmingPoolBasicInfoUseCase;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/swimming-pools/basic-info")
@RequiredArgsConstructor
public class FindSwimmingPoolBasicInfoController {

  private final FindSwimmingPoolBasicInfoUseCase useCase;

  @Operation(summary = "수영장 기본 정보 조회")
  @GetMapping
  public FindSwimmingPoolBasicInfoResponse findBasicInfo(
      @AuthenticationPrincipal TokenInfo tokenInfo) {

    return useCase.findSwimmingPoolBasicInfo(tokenInfo.swimmingPoolId());
  }
}
