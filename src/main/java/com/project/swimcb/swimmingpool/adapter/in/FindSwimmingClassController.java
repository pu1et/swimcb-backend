package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.application.in.FindSwimmingClassesUseCase;
import com.project.swimcb.swimmingpool.domain.FindSwimmingClassesCondition;
import com.project.swimcb.swimmingpool.domain.enums.GroupFixedClassSubTypeName;
import com.project.swimcb.swimmingpool.domain.enums.Sort;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/swimming-classes")
@RequiredArgsConstructor
public class FindSwimmingClassController {

  private final FindSwimmingClassesUseCase useCase;

  @Operation(summary = "수영 클래스 조회")
  @GetMapping
  public FindSwimmingClassesResponse findSwimmingClasses(

      @Parameter(description = "검색어", example = "냠뇸냠") @RequestParam(value = "keyword", required = false) String keyword,

      @Parameter(description = "검색 시작 날짜", example = "2025-03-01") @RequestParam(value = "start-date") LocalDate startDate,
      @Parameter(description = "검색 종료 날짜", example = "2025-03-31") @RequestParam(value = "end-date") LocalDate endDate,
      @Parameter(description = "검색 시작 시간 리스트", example = "06:00") @RequestParam(value = "start-times") @DateTimeFormat(pattern = "HH:mm") List<String> startTimes,

      @Parameter(description = "검색 요일 리스트", example = "MONDAY,TUESDAY,WEDNESDAY") @RequestParam(value = "days") List<DayOfWeek> days,

      @Parameter(description = "강습형태", example = "GROUP") @RequestParam(value = "class-types") List<SwimmingClassTypeName> classTypes,
      @Parameter(description = "강습구분", example = "BEGINNER") @RequestParam(value = "class-sub-types") List<GroupFixedClassSubTypeName> classSubTypes,

      @Parameter(description = "사용자 위도", example = "33.123456") @RequestParam(value = "member-latitude") double memberLatitude,
      @Parameter(description = "사용자 경도", example = "126.123456") @RequestParam(value = "member-longitude") double memberLongitude,

      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "10") int size,

      @RequestParam("sort") Sort sort,

      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {

    return useCase.findSwimmingClasses(FindSwimmingClassesCondition.builder()
        .keyword(keyword)
        .memberId(tokenInfo.memberId())
        .startDate(startDate)
        .endDate(endDate)
        .startTimes(startTimes.stream().map(LocalTime::parse).toList())
        .days(days)
        .classTypes(classTypes)
        .classSubTypes(classSubTypes)
        .memberLatitude(memberLatitude)
        .memberLongitude(memberLongitude)
        .pageable(PageRequest.of(page - 1, size))
        .sort(sort)
        .build());
  }
}
