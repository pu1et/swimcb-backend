package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.FreeSwimming;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "자유수영")
@RestController
@RequestMapping("/api/free-swimming")
public class FindFreeSwimmingController {

  @Operation(summary = "자유수영 조회")
  @GetMapping
  public List<FreeSwimming> findFreeSwimming(
      @Parameter(description = "위도", example = "37.5665") @RequestParam(value = "latitude") double latitude,
      @Parameter(description = "경도", example = "126.9780") @RequestParam(value = "longitude") double longitude,
      @Parameter(description = "줌 레벨", example = "12") @RequestParam(value = "zoomLevel") int zoomLevel,
      @Parameter(description = "오늘 가능 여부", example = "true") @RequestParam(value = "isAvailableToday", required = false) Boolean todayAvailable,
      @Parameter(description = "희망 일자", example = "2025-01-01") @RequestParam(value = "date") LocalDate date,
      @Parameter(description = "희망 시간대 시작", example = "06:00") @RequestParam(value = "start-time") @DateTimeFormat(pattern = "HH:mm") String startTime,
      @Parameter(description = "희망 시간대 종료", example = "10:00") @RequestParam(value = "end-time") @DateTimeFormat(pattern = "HH:mm") String endTime,
      @Parameter(description = "평점 최소", example = "4.0") @RequestParam(value = "min-star") double minStar,
      @Parameter(description = "평점 최대", example = "5.0") @RequestParam(value = "max-star") double maxStar
  ) {

    return List.of(
        FreeSwimming.builder()
            .swimmingPoolId(1L)
            .latitude(37.5665)
            .longitude(126.9780)
            .build(),
        FreeSwimming.builder()
            .swimmingPoolId(2L)
            .latitude(38.5665)
            .longitude(124.9780)
            .build(),
        FreeSwimming.builder()
            .swimmingPoolId(3L)
            .latitude(39.5665)
            .longitude(123.9780)
            .build()
    );
  }
}
