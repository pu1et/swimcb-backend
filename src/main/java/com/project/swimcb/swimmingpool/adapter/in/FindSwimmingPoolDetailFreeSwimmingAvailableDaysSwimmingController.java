package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.application.in.FindSwimmingPoolDetailFreeSwimmingAvailableDaysUseCase;
import com.project.swimcb.swimmingpool.domain.FindSwimmingPoolDetailFreeSwimmingAvailableDaysCondition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "수영장")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/swimming-pools/{swimmingPoolId}/free-swimming/available-days")
@RequiredArgsConstructor
@Validated
public class FindSwimmingPoolDetailFreeSwimmingAvailableDaysSwimmingController {

  private final FindSwimmingPoolDetailFreeSwimmingAvailableDaysUseCase useCase;

  @Operation(summary = "수영장 상세 조회 - 자유수영 가능 날짜 조회")
  @GetMapping
  public FindSwimmingPoolDetailFreeSwimmingAvailableDaysResponse findSwimmingPoolDetailFreeSwimmingAvailableDaysResponse(
      @PathVariable(value = "swimmingPoolId") @Min(1) long swimmingPoolId,
      @Parameter(description = "년/월", example = "2025-01") @RequestParam(value = "month") YearMonth month
  ) {

    val result = useCase.findSwimmingPoolDetailFreeSwimmingAvailableDays(
        FindSwimmingPoolDetailFreeSwimmingAvailableDaysCondition.builder()
            .swimmingPoolId(swimmingPoolId)
            .month(month)
            .build());
    return new FindSwimmingPoolDetailFreeSwimmingAvailableDaysResponse(result.days());
  }

}
