package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.application.in.FindSwimmingPoolDetailClassesUseCase;
import com.project.swimcb.swimmingpool.domain.enums.GroupFixedClassSubTypeName;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 - 완료")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/swimming-pools/{swimmingPoolId}/classes")
@RequiredArgsConstructor
public class FindSwimmingPoolDetailClassController {

  private final FindSwimmingPoolDetailClassesUseCase useCase;

  @Operation(summary = "수영장 상세 조회 - 클래스")
  @GetMapping
  public FindSwimmingPoolDetailClassesResponse findSwimmingPoolDetailFacility(
      @PathVariable(value = "swimmingPoolId") long swimmingPoolId,

      @Parameter(description = "검색 시작 날짜", example = "2025-03-01") @RequestParam(value = "start-date") LocalDate startDate,
      @Parameter(description = "검색 종료 날짜", example = "2025-03-31") @RequestParam(value = "end-date") LocalDate endDate,
      @Parameter(description = "검색 시작 시간 리스트", example = "06:00") @RequestParam(value = "start-times") @DateTimeFormat(pattern = "HH:mm") List<String> startTimes,

      @Parameter(description = "검색 요일 리스트", example = "MONDAY,TUESDAY,WEDNESDAY") @RequestParam(value = "days") List<DayOfWeek> days,

      @Parameter(description = "강습형태", example = "GROUP") @RequestParam(value = "class-types") List<SwimmingClassTypeName> classTypes,
      @Parameter(description = "강습구분", example = "BEGINNER") @RequestParam(value = "class-sub-types") List<GroupFixedClassSubTypeName> classSubTypes,

      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "10") int size,

      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
    return useCase.findSwimmingPoolDetailClasses(FindSwimmingPoolDetailClassesCondition.builder()
        .swimmingPoolId(swimmingPoolId)
        .startDate(startDate)
        .endDate(endDate)
        .startTimes(startTimes.stream().map(LocalTime::parse).toList())
        .days(days)
        .classTypes(classTypes)
        .classSubTypes(classSubTypes)
        .pageable(PageRequest.of(page - 1, size))
        .memberId(tokenInfo.memberId())
        .build());
  }
}
