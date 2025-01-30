package com.project.swimcb.mypage.reservation.adapter.in;

import com.project.swimcb.mypage.reservation.domain.ReservationDetail;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail.Payment;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail.Reservation;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail.SwimmingClass;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail.SwimmingPool;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "마이페이지")
@RestController
@RequestMapping("/api/my-page/reservations/{reservationId}")
public class FindReservationDetailController {

  @Operation(summary = "마이페이지 이용 내역 상세 조회")
  @GetMapping
  public ReservationDetail findReservationDetail(
      @PathVariable(name = "reservationId") Long reservationId) {
    return ReservationDetail.builder()
        .reservation(
            Reservation.builder()
                .waitingNumberFlag("대기번호 4번")
                .dateTime(LocalDateTime.of(2024, 7, 26, 18, 0))
                .build()
        )
        .swimmingPool(
            SwimmingPool.builder()
                .name("올림픽수영장")
                .address("서울시 송파구 올림픽로")
                .build()
        )
        .swimmingClass(
            SwimmingClass.builder()
                .type("단체강습")
                .subType("초급")
                .date("주 2일|화,목")
                .time("06:00 ~ 06:50")
                .price(90000)
                .build()
        )
        .payments(List.of(
            Payment.builder()
                .method("신용카드(우리/일시불)")
                .price(90000)
                .build()
        ))
        .build();

  }
}
