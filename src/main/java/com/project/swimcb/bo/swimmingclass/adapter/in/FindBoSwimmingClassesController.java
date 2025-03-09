package com.project.swimcb.bo.swimmingclass.adapter.in;

import com.project.swimcb.bo.swimmingclass.application.in.FindBoSwimmingClassesUseCase;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO - 완료")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/swimming-classes")
@RequiredArgsConstructor
public class FindBoSwimmingClassesController {

  private final FindBoSwimmingClassesUseCase useCase;

  @Operation(summary = "클래스 데이터 관리 - 클래스 전체 조회")
  @GetMapping
  public FindBoSwimmingClassesResponse findBoSwimmingClasses(
      @AuthenticationPrincipal TokenInfo tokenInfo,
      @Parameter(description = "월", example = "2") @Min(1) @Max(12) @RequestParam(value = "month") int month
  ) {
    return useCase.findBoSwimmingClasses(tokenInfo.swimmingPoolId(), month);
  }
}

