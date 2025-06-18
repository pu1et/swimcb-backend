package com.project.swimcb.reservation.adapter.in;

import static com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.CASH_ON_SITE;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.GROUP;
import static org.assertj.core.api.Assertions.assertThat;

import com.project.swimcb.db.entity.AccountNo;
import com.project.swimcb.reservation.domain.ReservationInfo;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindReservationResponseMapperTest {

  @InjectMocks
  private FindReservationResponseMapper mapper;

  @Nested
  @DisplayName("응답 변환 시")
  class ToResponseTest {

    @Test
    @DisplayName("예약 정보를 응답 객체로 올바르게 변환한다")
    void convertReservationInfoToResponseCorrectly() {
      // given
      val reservationInfo = TestReservationInfoFactory.create();

      // when
      val response = mapper.toResponse(reservationInfo);

      // then
      assertThat(response).isNotNull();

      // 수영장 정보 검증
      assertThat(response.swimmingPool().id()).isEqualTo(1L);
      assertThat(response.swimmingPool().name()).isEqualTo("DUMMY_POOL_NAME");
      assertThat(response.swimmingPool().accountNo()).isEqualTo("DUMMY_ACCOUNT_NO");

      // 수영 클래스 정보 검증
      assertThat(response.swimmingClass().id()).isEqualTo(2L);
      assertThat(response.swimmingClass().month()).isEqualTo(5);
      assertThat(response.swimmingClass().type()).isEqualTo(GROUP.getDescription());
      assertThat(response.swimmingClass().subType()).isEqualTo("DUMMY_CLASS_SUB_TYPE");
      assertThat(response.swimmingClass().days()).containsExactly("월", "수", "금");
      assertThat(response.swimmingClass().startTime()).isEqualTo(LocalTime.of(10, 0));
      assertThat(response.swimmingClass().endTime()).isEqualTo(LocalTime.of(11, 0));

      // 티켓 정보 검증
      assertThat(response.ticket().id()).isEqualTo(3L);
      assertThat(response.ticket().name()).isEqualTo("DUMMY_TICKET_NAME");
      assertThat(response.ticket().price()).isEqualTo(50000);

      // 예약 상태 검증
      assertThat(response.reservation().id()).isEqualTo(4L);
      assertThat(response.reservation().reservedAt()).isEqualTo(
          LocalDateTime.of(2025, 4, 1, 10, 0));
      assertThat(response.reservation().waitingNo()).isNull();

      // 결제 정보 검증
      assertThat(response.payment().method()).isEqualTo(CASH_ON_SITE.getDescription());
    }

    @Test
    @DisplayName("비트벡터로 표현된 요일을 요일명 리스트로 변환한다")
    void convertBitVectorToDaysCorrectly() {
      // given
      val mondayOnly = 0b1000000; // 월요일: 2^6
      val wednesdayOnly = 0b0010000; // 수요일: 2^4
      val mondayWednesdayFriday = 0b1010100; // 월,수,금: 2^6 + 2^4 + 2^2
      val allDays = 0b1111111; // 월화수목금토일: 127

      // when
      val mondayResult = mapper.bitVectorToDays(mondayOnly);
      val wednesdayResult = mapper.bitVectorToDays(wednesdayOnly);
      val mondayWednesdayFridayResult = mapper.bitVectorToDays(mondayWednesdayFriday);
      val allDaysResult = mapper.bitVectorToDays(allDays);

      // then
      assertThat(mondayResult).containsExactly("월");
      assertThat(wednesdayResult).containsExactly("수");
      assertThat(mondayWednesdayFridayResult).containsExactly("월", "수", "금");
      assertThat(allDaysResult).containsExactly("월", "화", "수", "목", "금", "토", "일");
    }

    private static class TestReservationInfoFactory {

      private static ReservationInfo create() {
        return ReservationInfo.builder()
            .swimmingPool(
                ReservationInfo.SwimmingPool.builder()
                    .id(1L)
                    .name("DUMMY_POOL_NAME")
                    .accountNo(AccountNo.of("DUMMY_ACCOUNT_NO"))
                    .build()
            )
            .swimmingClass(
                ReservationInfo.SwimmingClass.builder()
                    .id(2L)
                    .month(5)
                    .type(GROUP)
                    .subType("DUMMY_CLASS_SUB_TYPE")
                    .daysOfWeek(0b1010100) // 월,수,금
                    .startTime(LocalTime.of(10, 0))
                    .endTime(LocalTime.of(11, 0))
                    .build()
            )
            .ticket(
                ReservationInfo.Ticket.builder()
                    .id(3L)
                    .name("DUMMY_TICKET_NAME")
                    .price(50000)
                    .build()
            )
            .reservation(
                ReservationInfo.Reservation.builder()
                    .id(4L)
                    .reservedAt(LocalDateTime.of(2025, 4, 1, 10, 0))
                    .build()
            )
            .payment(
                ReservationInfo.Payment.builder()
                    .method(CASH_ON_SITE)
                    .build()
            )
            .build();
      }
    }
  }
}
