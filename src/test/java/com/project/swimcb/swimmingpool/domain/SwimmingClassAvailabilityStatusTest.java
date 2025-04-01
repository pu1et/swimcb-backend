package com.project.swimcb.swimmingpool.domain;

import static com.project.swimcb.swimmingpool.domain.SwimmingClassAvailabilityStatus.NOT_RESERVABLE;
import static com.project.swimcb.swimmingpool.domain.SwimmingClassAvailabilityStatus.RESERVABLE;
import static com.project.swimcb.swimmingpool.domain.SwimmingClassAvailabilityStatus.WAITING_RESERVABLE;
import static org.assertj.core.api.Assertions.assertThat;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SwimmingClassAvailabilityStatusTest {

  @Nested
  @DisplayName("수영 클래스 티켓의 예약 가능 상태는")
  class TicketReservationStatusTest {

    @Test
    @DisplayName("수강 정원에 여유가 있으면 '예약 가능' 상태이다")
    void availableSeatsForReservation() {
      // given
      val reservationLimitCount = 10;
      val reservedCount = 5;

      // when
      val status = SwimmingClassAvailabilityStatus.calculateStatus(reservationLimitCount,
          reservedCount);

      // then
      assertThat(status).isEqualTo(RESERVABLE);
    }

    @Test
    @DisplayName("수강 정원이 꽉 차면 '대기 가능' 상태이다")
    void waitingReservableWhenClassIsFull() {
      // given
      val reservationLimitCount = 20;
      val reservedCount = 20; // 정원 꽉 참

      // when
      val status = SwimmingClassAvailabilityStatus.calculateStatus(reservationLimitCount,
          reservedCount);

      // then
      assertThat(status).isEqualTo(WAITING_RESERVABLE);
    }

    @Test
    @DisplayName("수강 정원이 꽉 차도 대기자 3명까지는 '대기 가능' 상태이다")
    void waitingReservableWhenClassIsFullWithWaitlist() {
      // given
      val reservationLimitCount = 20;
      val reservedCount = 23; // 정원(20명) + 대기자 3명

      // when
      val status = SwimmingClassAvailabilityStatus.calculateStatus(reservationLimitCount,
          reservedCount);

      // then
      assertThat(status).isEqualTo(WAITING_RESERVABLE);
    }

    @Test
    @DisplayName("수강 정원도 꽉 차고 대기자도 4명이 차면 '예약 불가' 상태이다")
    void notReservableWhenClassIsFullAndWaitlistIsFull() {
      // given
      val reservationLimitCount = 20;
      val reservedCount = 24; // 정원(20명) + 대기자 최대(4명)

      // when
      val status = SwimmingClassAvailabilityStatus.calculateStatus(reservationLimitCount,
          reservedCount);

      // then
      assertThat(status).isEqualTo(NOT_RESERVABLE);
    }

    @ParameterizedTest(name = "정원 {0}명, 현재 {1}명 등록 시 상태는 {2}")
    @DisplayName("다양한 등록 인원에 따른 예약 상태 확인")
    @CsvSource({
        "20, 0, RESERVABLE",     // 빈 클래스
        "20, 19, RESERVABLE",    // 1자리 남음
        "20, 20, WAITING_RESERVABLE", // 정원 꽉 참 (대기 가능)
        "20, 21, WAITING_RESERVABLE", // 대기 1명
        "20, 23, WAITING_RESERVABLE", // 대기 3명 (경계값)
        "20, 24, NOT_RESERVABLE",    // 대기 불가
        "20, 30, NOT_RESERVABLE"     // 대기 불가 (초과)
    })
    void reservationStatusByEnrollmentCount(int capacity, int enrolled,
        SwimmingClassAvailabilityStatus expected) {

      // given
      // when
      val status = SwimmingClassAvailabilityStatus.calculateStatus(capacity, enrolled);

      // then
      assertThat(status).isEqualTo(expected);
    }
  }
}