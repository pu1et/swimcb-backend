package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailClass;
import com.project.swimcb.swimmingpool.domain.enums.ClassType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "수영장")
@RestController
@RequestMapping("/api/swimming-pools/{swimmingPoolId}/classes")
public class FindSwimmingPoolDetailClassController {

  @Operation(summary = "수영장 상세 조회 - 클래스")
  @GetMapping
  public List<SwimmingPoolDetailClass> findSwimmingPoolDetailFacility(
      @PathVariable(value = "swimmingPoolId") long swimmingPoolId,
      @Parameter(description = "시간대") @RequestParam("class-time") LocalDateTime classTime,
      @Parameter(description = "강습 요일", example = "MONDAY") @RequestParam("class-day") DayOfWeek classDay,
      @Parameter(description = "강습형태", example = "GROUP_BEGINNER") @RequestParam("class-type") ClassType classType
  ) {
    return List.of(
        SwimmingPoolDetailClass.builder()
            .name("일반수영")
            .classType("초급")
            .classDay("주 3일|월,수,금")
            .classTime("06:00 ~ 06:50")
            .price(90000)
            .isClassClosed(false)
            .build(),
        SwimmingPoolDetailClass.builder()
            .name("일반수영")
            .classType("초급")
            .classDay("주 3일|월,수,금")
            .classTime("06:00 ~ 06:50")
            .price(90000)
            .isClassClosed(false)
            .build(),
        SwimmingPoolDetailClass.builder()
            .name("일반수영")
            .classType("초급")
            .classDay("주 2일|화,목")
            .classTime("09:00 ~ 09:50")
            .price(90000)
            .isClassClosed(true)
            .build()
    );
  }
}
