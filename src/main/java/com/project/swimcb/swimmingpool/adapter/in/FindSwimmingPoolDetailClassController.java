package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailClass;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/swimming-pools/{swimmingPoolId}/classes")
public class FindSwimmingPoolDetailClassController {

  @Operation(summary = "수영장 상세 조회 - 클래스")
  @GetMapping
  public List<SwimmingPoolDetailClass> findSwimmingPoolDetailFacility(
      @PathVariable(value = "swimmingPoolId") long swimmingPoolId,
      @Parameter(description = "시간대", example = "2025-01-01T01:01:01") @RequestParam(value = "class-time", required = false) LocalDateTime classTime,
      @Parameter(description = "강습 요일", example = "MONDAY") @RequestParam(value = "class-day", required = false) DayOfWeek classDay,
      @Parameter(description = "강습형태", example = "GROUP_BEGINNER") @RequestParam(value = "swimmingClassType", required = false) SwimmingClassType swimmingClassType
  ) {
    return List.of(
        SwimmingPoolDetailClass.builder()
            .swimmingClassId(1L)
            .name("일반수영")
            .type("초급")
            .day("주 3일|월,수,금")
            .time("06:00 ~ 06:50")
            .price(90000)
            .isReservable(false)
            .build(),
        SwimmingPoolDetailClass.builder()
            .swimmingClassId(2L)
            .name("일반수영")
            .type("초급")
            .day("주 3일|월,수,금")
            .time("06:00 ~ 06:50")
            .price(90000)
            .isReservable(false)
            .build(),
        SwimmingPoolDetailClass.builder()
            .swimmingClassId(3L)
            .name("일반수영")
            .type("초급")
            .day("주 2일|화,목")
            .time("09:00 ~ 09:50")
            .price(90000)
            .isReservable(true)
            .build()
    );
  }
}
