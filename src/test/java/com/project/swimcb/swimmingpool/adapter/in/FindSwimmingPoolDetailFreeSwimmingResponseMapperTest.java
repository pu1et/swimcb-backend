package com.project.swimcb.swimmingpool.adapter.in;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimming;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimming.FreeSwimming;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimming.Ticket;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimming.Time;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FindSwimmingPoolDetailFreeSwimmingResponseMapperTest {

  private final FindSwimmingPoolDetailFreeSwimmingResponseMapper mapper =
      new FindSwimmingPoolDetailFreeSwimmingResponseMapper();

  @Test
  @DisplayName("유효한 SwimmingPoolDetailFreeSwimming을 Response로 변환한다")
  void shouldMapToResponseSuccessfully() {
    // given
    val time = Time.builder()
        .startTime(LocalTime.of(9, 0))
        .endTime(LocalTime.of(10, 0))
        .build();

    val ticket = Ticket.builder()
        .name("성인권")
        .price(5000)
        .build();

    val freeSwimming = FreeSwimming.builder()
        .time(time)
        .daysOfWeek(new ClassDayOfWeek(List.of(MONDAY, TUESDAY, WEDNESDAY)))
        .ticket(ticket)
        .build();

    val domain = new SwimmingPoolDetailFreeSwimming(List.of(freeSwimming));

    // when
    val response = mapper.toResponse(domain);

    // then
    assertThat(response.freeSwimmings()).hasSize(1);

    val content = response.freeSwimmings().getFirst();
    assertThat(content.time().startTime()).isEqualTo(time.startTime());
    assertThat(content.time().endTime()).isEqualTo(time.endTime());
    assertThat(content.days()).isEqualTo(List.of("월", "화", "수"));
    assertThat(content.ticket().name()).isEqualTo(ticket.name());
    assertThat(content.ticket().price()).isEqualTo(ticket.price());
  }

  @Test
  @DisplayName("null이 전달되면 예외를 발생시킨다")
  void shouldThrowExceptionWhenNullPassed() {
    // when & then
    assertThatThrownBy(() -> mapper.toResponse(null))
        .isInstanceOf(NullPointerException.class);
  }

}
