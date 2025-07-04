package com.project.swimcb.bo.freeswimming.adapter.in;

import com.project.swimcb.bo.freeswimming.adapter.out.FindBoFreeSwimmingResponseMapper;
import com.project.swimcb.bo.freeswimming.application.port.in.FindBoFreeSwimmingUseCase;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO - 완료")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/free-swimming")
@RequiredArgsConstructor
public class FindBoFreeSwimmingController {

  private final FindBoFreeSwimmingUseCase useCase;
  private final FindBoFreeSwimmingResponseMapper responseMapper;

  @Operation(summary = "자유수영 데이터 관리 - 자유수영 리스트 조회")
  @GetMapping
  public FindBoFreeSwimmingResponse findBoFreeSwimming(
      @RequestParam(value = "yearMonth") @Parameter(description = "조회연월", example = "2025-06") YearMonth yearMonth,
      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
    val freeSwimmings = useCase.findBoFreeSwimming(
        tokenInfo.swimmingPoolId(),
        yearMonth.atDay(1)
    );
    return responseMapper.toResponse(freeSwimmings);
  }

}

