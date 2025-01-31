package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.FreeSwimmingReservationInfo;
import com.project.swimcb.swimmingpool.domain.FreeSwimmingReservationInfo.FreeSwimming;
import com.project.swimcb.swimmingpool.domain.FreeSwimmingReservationInfo.SwimmingPool;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "클래스")
@RestController
@RequestMapping("/api/free-swimming/{freeSwimmingId}/reservations")
public class FindFreeSwimmingReservationInfoController {

  @Operation(summary = "자유수영 예약 정보 조회")
  @GetMapping
  public FreeSwimmingReservationInfo findFreeSwimmingReservationInfo(
      @PathVariable(value = "freeSwimmingId") long freeSwimmingId
  ) {
    return FreeSwimmingReservationInfo.builder()
        .swimmingPool(
            SwimmingPool.builder()
                .name("올림픽 수영장")
                .address("서울시 송파구 올림픽로")
                .build()
        )
        .freeSwimming(
            FreeSwimming.builder()
                .name("일일권")
                .dateTime("2025-01-01 06:00")
                .price(90000)
                .consentAgreement("이용 동의서 HTML")
                .build()
        )
        .build();
  }
}
