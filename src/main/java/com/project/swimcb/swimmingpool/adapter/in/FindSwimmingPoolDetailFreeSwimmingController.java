package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.FindSwimmingPoolDetailFreeSwimmingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "수영장")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/swimming-pools/{swimmingPoolId}/free-swimming")
@Validated
public class FindSwimmingPoolDetailFreeSwimmingController {

  @Operation(summary = "수영장 상세 조회 - 자유수영")
  @GetMapping
  public FindSwimmingPoolDetailFreeSwimmingResponse findSwimmingPoolDetailFreeSwimming(
      @PathVariable(value = "swimmingPoolId") @Min(1) long swimmingPoolId,
      @Parameter(description = "날짜", example = "2025-01-01") @RequestParam(value = "date") LocalDate date
  ) {

    return null;
  }

}
