package com.project.swimcb.bo.freeswimming.adapter.out;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.swimcb.bo.freeswimming.domain.BoFreeSwimming;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindBoFreeSwimmingResponseMapperTest {

  @InjectMocks
  private FindBoFreeSwimmingResponseMapper mapper;

  @Test
  @DisplayName("모든 필드가 있는 리스트가 주어진 경우 모든 데이터를 올바르게 매핑한다")
  void shouldMapAllFieldsCorrectly() {
    // given
    val freeSwimming = TestBoFreeSwimmingFactory.createCompleteBoFreeSwimming();

    // when
    val response = mapper.toResponse(List.of(freeSwimming));

    // then
    val result = response.freeSwimmings().getFirst();
    assertThat(result.freeSwimmingId()).isEqualTo(1L);
    assertThat(result.days()).containsExactly(DayOfWeek.MONDAY);
    assertThat(result.time().startTime()).isEqualTo(LocalTime.of(6, 0));
    assertThat(result.lifeguard().name()).isEqualTo("김강사");
    assertThat(result.tickets()).hasSize(1);
    assertThat(result.capacity()).isEqualTo(20);
  }

  @Test
  @DisplayName("라이프가드가 없는 경우 라이프가드 필드는 null이어야 한다")
  void shouldReturnNullLifeguardWhenNotPresent() {
    // given
    val freeSwimming = TestBoFreeSwimmingFactory.createBoFreeSwimmingWithoutLifeguard();

    // when
    val response = mapper.toResponse(List.of(freeSwimming));

    // then
    assertThat(response.freeSwimmings().getFirst().lifeguard()).isNull();
  }

  private static class TestBoFreeSwimmingFactory {

    private static BoFreeSwimming createCompleteBoFreeSwimming() {
      return BoFreeSwimming.builder()
          .freeSwimmingId(1L)
          .days(List.of(DayOfWeek.MONDAY))
          .time(new BoFreeSwimming.Time(LocalTime.of(6, 0), LocalTime.of(7, 0)))
          .lifeguard(new BoFreeSwimming.Lifeguard(1L, "김강사"))
          .ticketPriceRange(new BoFreeSwimming.TicketPriceRange(1000, 5000))
          .tickets(List.of(new BoFreeSwimming.Ticket(1L, "일반권", 3000)))
          .capacity(20)
          .isExposed(true)
          .build();
    }

    private static BoFreeSwimming createBoFreeSwimmingWithoutLifeguard() {
      return BoFreeSwimming.builder()
          .freeSwimmingId(2L)
          .days(List.of(DayOfWeek.TUESDAY))
          .time(new BoFreeSwimming.Time(LocalTime.of(8, 0), LocalTime.of(9, 0)))
          .ticketPriceRange(new BoFreeSwimming.TicketPriceRange(2000, 6000))
          .tickets(List.of(new BoFreeSwimming.Ticket(2L, "학생권", 4000)))
          .capacity(30)
          .isExposed(false)
          .build();
    }

  }

}
