package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.FindRecommendationSwimmingPoolResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "추천")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/main/recommendation")
public class FindRecommendationSwimmingPoolController {

  @Operation(summary = "추천 수영장 조회")
  @GetMapping
  public FindRecommendationSwimmingPoolResponse findRecommendationSwimmingPool() {
    return null;
  }

}
