package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.FindFreeSwimmingCondition;
import com.project.swimcb.swimmingpool.application.in.FindFreeSwimmingUseCase;
import com.project.swimcb.swimmingpool.domain.FindFreeSwimmingResponse;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "자유수영")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/free-swimming")
@RequiredArgsConstructor
public class FindFreeSwimmingController {

  private final FindFreeSwimmingUseCase useCase;
  private final FindFreeSwimmingResponseMapper responseMapper;

  @Operation(summary = "자유수영 조회")
  @GetMapping
  public FindFreeSwimmingResponse findFreeSwimming(
      @Parameter(description = "회원 위도", example = "37.5665") @RequestParam(value = "memberLatitude") double memberLatitude,
      @Parameter(description = "회원 경도", example = "126.9780") @RequestParam(value = "memberLongitude") double memberLongitude,
      @Parameter(description = "좌측 상단 위도", example = "37.5665") @RequestParam(value = "minLatitude") double minLatitude,
      @Parameter(description = "우측 하단 위도", example = "38.5665") @RequestParam(value = "maxLatitude") double maxLatitude,
      @Parameter(description = "좌측 상단 경도", example = "126.9780") @RequestParam(value = "minLongitude") double minLongitude,
      @Parameter(description = "우측 하단 경도", example = "127.9780") @RequestParam(value = "maxLongitude") double maxLongitude,
      @Parameter(description = "오늘 가능 여부", example = "true") @RequestParam(value = "isTodayAvailable") boolean isTodayAvailable,
      @Parameter(description = "희망 일자", example = "2025-01-01") @RequestParam(value = "date", required = false) LocalDate date,
      @Parameter(description = "희망 시간대 시작", example = "06:00") @RequestParam(value = "start-times") @DateTimeFormat(pattern = "HH:mm") List<String> startTimes,

      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {

    val result = useCase.findFreeSwimming(
        FindFreeSwimmingCondition.builder()
            .memberId(tokenInfo.memberId())
            .memberLatitude(memberLatitude)
            .memberLongitude(memberLongitude)
            .minLatitude(minLatitude)
            .maxLatitude(maxLatitude)
            .minLongitude(minLongitude)
            .maxLongitude(maxLongitude)
            .isTodayAvailable(isTodayAvailable)
            .date(date)
            .startTimes(startTimes.stream().map(LocalTime::parse).toList())
            .build()
    );
    return responseMapper.toResponse(result);
  }

}
