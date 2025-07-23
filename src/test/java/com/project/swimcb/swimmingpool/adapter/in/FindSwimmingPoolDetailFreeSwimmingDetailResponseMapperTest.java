package com.project.swimcb.swimmingpool.adapter.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimmingDetail;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimmingDetail.FreeSwimming;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimmingDetail.Ticket;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimmingDetail.Time;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindSwimmingPoolDetailFreeSwimmingDetailResponseMapperTest {

  @InjectMocks
  private FindSwimmingPoolDetailFreeSwimmingDetailResponseMapper mapper;

  @Test
  @DisplayName("유효한 SwimmingPoolDetailFreeSwimmingDetail을 Response로 변환한다")
  void shouldMapToResponseSuccessfully() {
    // given
    val time = Time.builder()
        .startTime(LocalTime.of(9, 0))
        .endTime(LocalTime.of(10, 0))
        .build();

    val ticket = Ticket.builder()
        .id(1L)
        .name("성인권")
        .price(5000)
        .build();

    val freeSwimming = FreeSwimming.builder()
        .dayStatusId(100L)
        .time(time)
        .minTicketPrice(3000)
        .tickets(List.of(ticket))
        .favoriteId(200L)
        .build();

    val detail = new SwimmingPoolDetailFreeSwimmingDetail(List.of(freeSwimming));

    // when
    val response = mapper.toResponse(detail);

    // then
    assertThat(response.freeSwimmings()).hasSize(1);

    val content = response.freeSwimmings().getFirst();
    assertThat(content.dayStatusId()).isEqualTo(freeSwimming.dayStatusId());
    assertThat(content.time().startTime()).isEqualTo(freeSwimming.time().startTime());
    assertThat(content.time().endTime()).isEqualTo(freeSwimming.time().endTime());
    assertThat(content.minTicketPrice()).isEqualTo(freeSwimming.minTicketPrice());

    assertThat(content.tickets()).hasSize(1);

    val firstTicket = content.tickets().getFirst();
    assertThat(firstTicket.id()).isEqualTo(ticket.id());
    assertThat(firstTicket.name()).isEqualTo(ticket.name());
    assertThat(firstTicket.price()).isEqualTo(ticket.price());

    assertThat(content.favoriteId()).isEqualTo(freeSwimming.favoriteId());
  }

  @Test
  @DisplayName("null이 전달되면 예외를 발생시킨다")
  void shouldThrowExceptionWhenNullPassed() {
    // when & then
    assertThatThrownBy(() -> mapper.toResponse(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("빈 리스트가 있는 SwimmingPoolDetailFreeSwimmingDetail을 변환한다")
  void shouldMapEmptyListSuccessfully() {
    // given
    val detail = new SwimmingPoolDetailFreeSwimmingDetail(List.of());

    // when
    val response = mapper.toResponse(detail);

    // then
    assertThat(response.freeSwimmings()).isEmpty();
  }

}
