package com.project.swimcb.bo.instructor.adapter.in;

import com.project.swimcb.bo.instructor.application.in.FindBoInstructorsUseCase;
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
@RequestMapping("/api/bo/instructors")
@RequiredArgsConstructor
public class FindBoInstructorsController {

  private final FindBoInstructorsUseCase useCase;

  @Operation(summary = "클래스 데이터 관리 - 강사 리스트 조회")
  @GetMapping
  public FindBoInstructorsResponse findBoInstructors(
      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
    return useCase.findBoInstructors(tokenInfo.swimmingPoolId());
  }
}

