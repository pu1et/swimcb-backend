package com.project.swimcb.main.adapter.in;

import com.project.swimcb.main.domain.FindRecommendationSwimmingPoolCondition;
import com.project.swimcb.main.application.port.in.FindRecommendationSwimmingPoolUseCase;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "추천")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/main/recommendation")
@RequiredArgsConstructor
public class FindRecommendationSwimmingPoolController {

  private final FindRecommendationSwimmingPoolUseCase useCase;
  private final FindRecommendationSwimmingPoolResponseMapper responseMapper;

  @Operation(summary = "추천 수영장 조회")
  @GetMapping
  public FindRecommendationSwimmingPoolResponse findRecommendationSwimmingPool(
      @Parameter(description = "사용자 위도", example = "33.123456") @RequestParam(value = "member-latitude") double memberLatitude,
      @Parameter(description = "사용자 경도", example = "126.123456") @RequestParam(value = "member-longitude") double memberLongitude,

      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
    val result = useCase.findRecommendationSwimmingPools(
        FindRecommendationSwimmingPoolCondition.builder()
            .memberId(tokenInfo.memberId())
            .memberLatitude(memberLatitude)
            .memberLongitude(memberLongitude)
            .build()
    );
    return responseMapper.toResponse(result);
  }

}
