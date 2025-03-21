package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.application.in.FindSwimmingPoolDetailMainUseCase;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 - 완료")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/swimming-pools/{swimmingPoolId}/main")
@RequiredArgsConstructor
public class FindSwimmingPoolDetailMainController {

  private final FindSwimmingPoolDetailMainUseCase useCase;
  private final FindSwimmingPoolDetailMainResponseFactory responseFactory;

  @Operation(summary = "수영장 상세 조회 - 메인정보")
  @GetMapping
  public FindSwimmingPoolDetailMainResponse findSwimmingPoolDetailMain(
      @PathVariable(value = "swimmingPoolId") long swimmingPoolId,

      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
    return responseFactory.create(useCase.findSwimmingPoolDetailMain(
        swimmingPoolId, tokenInfo.memberId()));
  }
}
