package com.project.swimcb.bo.swimmingclass.adapter.in;

import com.project.swimcb.bo.swimmingclass.application.in.FindBoSwimmingClassTypesUseCase;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO - 완료")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/swimming-classes/class-types")
@RequiredArgsConstructor
public class FindBoSwimmingClassTypesController {

  private final FindBoSwimmingClassTypesUseCase useCase;

  @Operation(summary = "클래스 데이터 관리 - 클래스 강습형태/구분 리스트 조회")
  @GetMapping
  public FindBoSwimmingClassTypesResponse findBoSwimmingClassSubType(
      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
    return useCase.findBoSwimmingClassTypes(tokenInfo.swimmingPoolId());
  }
}

