package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.SwimmingClass;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.project.swimcb.swimmingpool.domain.enums.Sort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "클래스")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/swimming-classes")
public class FindSwimmingClassController {

  @Operation(summary = "수영 클래스 조회")
  @GetMapping
  public List<SwimmingClass> findSwimmingClasses(
      @Parameter(description = "시간대", example = "2025-01-01T01:01:01") @RequestParam(value = "class-time", required = false) LocalDateTime classTime,
      @Parameter(description = "강습 요일", example = "MONDAY") @RequestParam(value = "class-day", required = false) DayOfWeek classDay,
      @Parameter(description = "강습형태", example = "GROUP_BEGINNER") @RequestParam(value = "swimmingClassType", required = false) SwimmingClassTypeName swimmingClassType,
      @Parameter(description = "마감 클래스 제외 여부", example = "true") @RequestParam("exclude-closed-class") boolean excludeClosedClass,
      @RequestParam("sort") Sort sort
  ) {

    return List.of(
        SwimmingClass.builder()
            .swimmingPoolId(1L)
            .imageUrl("https://ibb.co/bjGKF8WV")
            .isFavorite(false)
            .distance("400m")
            .name("올림픽 수영장")
            .address("서울시 송파구 올림픽로")
            .lowestPrice(10000)
            .star("4.5")
            .reviewCount(35)
            .build(),
        SwimmingClass.builder()
            .swimmingPoolId(2L)
            .imageUrl("https://ibb.co/67BwJXjC")
            .isFavorite(false)
            .distance("820m")
            .name("킨디스윔")
            .address("서울시 송파구 양재대로")
            .lowestPrice(15000)
            .star("4.0")
            .reviewCount(28)
            .build(),
        SwimmingClass.builder()
            .swimmingPoolId(3L)
            .imageUrl("https://ibb.co/zWdrk20p")
            .isFavorite(true)
            .distance("970m")
            .name("웨일즈수영장")
            .address("서울시 송파구 올림픽로")
            .lowestPrice(20000)
            .star("4.1")
            .reviewCount(9)
            .build()
    );
  }
}
