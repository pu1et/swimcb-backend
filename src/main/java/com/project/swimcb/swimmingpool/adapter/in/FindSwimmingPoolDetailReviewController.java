package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailReview;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "수영장")
@RestController
@RequestMapping("/swimming-pool/{swimmingPoolId}/review")
public class FindSwimmingPoolDetailReviewController {

  @Operation(summary = "수영장 상세 조회 - 후기")
  @GetMapping
  public SwimmingPoolDetailReview findSwimmingPoolDetailReview(
      @PathVariable(value = "swimmingPoolId") long swimmingPoolId
  ) {
    return SwimmingPoolDetailReview.builder()
        .star("4.5")
        .writeDate(LocalDate.of(2024, 7, 28))
        .memberId("swimlover")
        .content("방문해봤는데 깨끗하고 청결해서 좋았습니다.")
        .build();
  }
}
