package com.project.swimcb.bo.freeswimming.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.freeswimming.application.port.out.FindBoFreeSwimmingDsGateway;
import com.project.swimcb.bo.freeswimming.domain.BoFreeSwimming;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindBoFreeSwimmingInteractorTest {

  @Mock
  private FindBoFreeSwimmingDsGateway gateway;

  @InjectMocks
  private FindBoFreeSwimmingInteractor interactor;

  private static final Long SWIMMING_POOL_ID = 1L;
  private static final LocalDate YEAR_MONTH = LocalDate.of(2025, 1, 1);

  @Nested
  @DisplayName("자유수영 조회 시")
  class DescribeFindBoFreeSwimming {

    @Nested
    @DisplayName("유효한 수영장 ID와 날짜가 주어진 경우")
    class ContextWithValidParameters {

      @Test
      @DisplayName("게이트웨이를 통해 자유수영 목록을 조회한다")
      void shouldFindFreeSwimmingsThroughGateway() {
        // given
        val expectedFreeSwimmings = List.of(
            createFreeSwimmingWithLifeguard(),
            createFreeSwimmingWithoutLifeguard()
        );

        when(gateway.findBoFreeSwimming(SWIMMING_POOL_ID, YEAR_MONTH))
            .thenReturn(expectedFreeSwimmings);

        // when
        val result = interactor.findBoFreeSwimming(SWIMMING_POOL_ID, YEAR_MONTH);

        // then
        assertThat(result).isEqualTo(expectedFreeSwimmings);

        verify(gateway, only()).findBoFreeSwimming(SWIMMING_POOL_ID, YEAR_MONTH);
      }

      @Test
      @DisplayName("조회된 자유수영이 없으면 빈 목록을 반환한다")
      void shouldReturnEmptyListWhenNoFreeSwimmingsFound() {
        // given
        when(gateway.findBoFreeSwimming(SWIMMING_POOL_ID, YEAR_MONTH))
            .thenReturn(List.of());

        // when
        val result = interactor.findBoFreeSwimming(SWIMMING_POOL_ID, YEAR_MONTH);

        // then
        assertThat(result).isEmpty();

        verify(gateway, only()).findBoFreeSwimming(SWIMMING_POOL_ID, YEAR_MONTH);
      }
    }

    @Nested
    @DisplayName("수영장 ID가 null인 경우")
    class ContextWithNullSwimmingPoolId {

      @Test
      @DisplayName("NullPointerException을 발생시킨다")
      void shouldThrowNullPointerException() {
        // given
        // when
        // then
        assertThatThrownBy(() -> interactor.findBoFreeSwimming(null, YEAR_MONTH))
            .isInstanceOf(NullPointerException.class);
      }
    }

    @Nested
    @DisplayName("날짜가 null인 경우")
    class ContextWithNullYearMonth {

      @Test
      @DisplayName("NullPointerException을 발생시킨다")
      void shouldThrowNullPointerException() {
        // given
        // when
        // then
        assertThatThrownBy(() -> interactor.findBoFreeSwimming(SWIMMING_POOL_ID, null))
            .isInstanceOf(NullPointerException.class);
      }
    }

    @Nested
    @DisplayName("게이트웨이에서 예외가 발생한 경우")
    class ContextWithGatewayException {

      @Test
      @DisplayName("게이트웨이 예외를 그대로 전파한다")
      void shouldPropagateGatewayException() {
        // given
        val expectedException = new RuntimeException("데이터베이스 연결 실패");
        when(gateway.findBoFreeSwimming(SWIMMING_POOL_ID, YEAR_MONTH))
            .thenThrow(expectedException);

        // when
        // then
        assertThatThrownBy(() -> interactor.findBoFreeSwimming(SWIMMING_POOL_ID, YEAR_MONTH))
            .isEqualTo(expectedException);
      }
    }
  }

  private BoFreeSwimming createFreeSwimmingWithLifeguard() {
    return BoFreeSwimming.builder()
        .freeSwimmingId(1L)
        .days(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY))
        .time(new BoFreeSwimming.Time(LocalTime.of(6, 0), LocalTime.of(6, 50)))
        .lifeguard(new BoFreeSwimming.Lifeguard(1L, "김강사"))
        .tickets(List.of(new BoFreeSwimming.Ticket(1L, "성인일반", 10000)))
        .ticketPriceRange(new BoFreeSwimming.TicketPriceRange(10000, 10000))
        .capacity(20)
        .isExposed(true)
        .build();
  }

  private BoFreeSwimming createFreeSwimmingWithoutLifeguard() {
    return BoFreeSwimming.builder()
        .freeSwimmingId(2L)
        .days(List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY))
        .time(new BoFreeSwimming.Time(LocalTime.of(7, 0), LocalTime.of(7, 50)))
        .lifeguard(null)
        .tickets(List.of(new BoFreeSwimming.Ticket(2L, "성인일반", 8000)))
        .ticketPriceRange(new BoFreeSwimming.TicketPriceRange(8000, 8000))
        .capacity(15)
        .isExposed(true)
        .build();
  }
}
