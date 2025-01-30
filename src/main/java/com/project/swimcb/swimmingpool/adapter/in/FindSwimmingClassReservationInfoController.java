package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.SwimmingClassReservationInfo;
import com.project.swimcb.swimmingpool.domain.SwimmingClassReservationInfo.SwimmingClass;
import com.project.swimcb.swimmingpool.domain.SwimmingClassReservationInfo.SwimmingPool;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "클래스")
@RestController
@RequestMapping("/api/swimming-classes/{swimmingClassId}/reservations")
public class FindSwimmingClassReservationInfoController {

  @Operation(summary = "수영 클래스 예약 정보 조회")
  @GetMapping
  public SwimmingClassReservationInfo findSwimmingClassReservationInfo(
      @PathVariable(value = "swimmingClassId") long swimmingClassId
  ) {
    return SwimmingClassReservationInfo.builder()
        .swimmingPool(
            SwimmingPool.builder()
                .name("올림픽 수영장")
                .address("서울시 송파구 올림픽로")
                .build()
        )
        .swimmingClass(
            SwimmingClass.builder()
                .type("수영 기초반")
                .day("주 5일 | 월~금")
                .time("06:00 ~ 06:50")
                .price(90000)
                .consentAgreement("이용 동의서 HTML")
                .build()
        )
        .build();
  }
}
