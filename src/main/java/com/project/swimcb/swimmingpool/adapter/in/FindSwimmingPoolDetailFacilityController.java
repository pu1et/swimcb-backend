package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.adapter.out.FindSwimmingPoolDetailFacilityResponseMapper;
import com.project.swimcb.swimmingpool.application.in.FindSwimmingPoolDetailFacilityUseCase;
import com.project.swimcb.swimmingpool.domain.FindSwimmingPoolDetailFacilityResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "수영장")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/swimming-pools/{swimmingPoolId}/facility")
@RequiredArgsConstructor
@Validated
public class FindSwimmingPoolDetailFacilityController {

  private final FindSwimmingPoolDetailFacilityUseCase useCase;
  private final FindSwimmingPoolDetailFacilityResponseMapper responseMapper;

  @Operation(summary = "수영장 상세 조회 - 시설정보")
  @GetMapping
  public FindSwimmingPoolDetailFacilityResponse findSwimmingPoolDetailFacility(
      @Min(1) @PathVariable(value = "swimmingPoolId") long swimmingPoolId
  ) {
    val result = useCase.findSwimmingPoolDetailFacility(swimmingPoolId);
    return responseMapper.toResponse(result);
  }

}
