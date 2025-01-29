package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFacility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "수영장")
@RestController
@RequestMapping("/swimming-pool/{swimmingPoolId}/facility")
public class FindSwimmingPoolDetailFacilityController {

  @Operation(summary = "수영장 상세 조회 - 시설정보")
  @GetMapping
  public SwimmingPoolDetailFacility findSwimmingPoolDetailFacility(
      @PathVariable(value = "swimmingPoolId") long swimmingPoolId
  ) {
    return SwimmingPoolDetailFacility.builder()
        .openingHours("연중무휴 06:00 - 23:30")
        .location("스포츠센터 지하 1층")
        .size("성인 수영장(25m * 13m, 6레인)")
        .depth("성인 수영장 120 - 140cm")
        .program("주부수영 A, B | 일일 자유수영")
        .maintenance("여과 시스템 1일 4회 | 구청 보건소")
        .build();
  }
}
