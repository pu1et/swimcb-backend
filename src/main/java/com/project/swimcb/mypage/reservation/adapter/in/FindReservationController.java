package com.project.swimcb.mypage.reservation.adapter.in;

import com.project.swimcb.mypage.reservation.domain.Reservation;
import com.project.swimcb.mypage.reservation.domain.Reservation.Swimming;
import com.project.swimcb.mypage.reservation.domain.Reservation.SwimmingPool;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "마이페이지")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/my-page/reservations")
public class FindReservationController {

  @Operation(summary = "마이페이지 이용 내역 조회")
  @GetMapping
  public List<Reservation> findReservations() {
    return List.of(
        Reservation.builder()
            .reservationId(1L)
            .date(LocalDate.of(2024, 8, 28))
            .status("예약 대기")
            .waitingNumberFlag("대기번호 4번")
            .swimmingPool(
                SwimmingPool.builder()
                    .imageUrl("https://ibb.co/bjGKF8WV")
                    .name("올림픽 수영장")
                    .build()
            )
            .swimming(
                Swimming.builder()
                    .type("단체강습 초급")
                    .price(90000)
                    .build()
            )
            .build(),
        Reservation.builder()
            .reservationId(2L)
            .date(LocalDate.of(2024, 7, 21))
            .status("예약 완료")
            .swimmingPool(
                SwimmingPool.builder()
                    .imageUrl("https://ibb.co/bjGKF8WV")
                    .name("올림픽 수영장")
                    .build()
            )
            .swimming(
                Swimming.builder()
                    .type("자유수영 일일권")
                    .price(7000)
                    .build()
            )
            .build(),
        Reservation.builder()
            .reservationId(3L)
            .date(LocalDate.of(2024, 7, 20))
            .status("취소 완료")
            .swimmingPool(
                SwimmingPool.builder()
                    .imageUrl("https://ibb.co/zWdrk20p")
                    .name("웨일즈수영장")
                    .build()
            )
            .swimming(
                Swimming.builder()
                    .type("자유수영 일일권")
                    .price(9000)
                    .build()
            )
            .build()
    );
  }
}
