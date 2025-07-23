package com.project.swimcb.swimmingpool.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.project.swimcb.swimmingpool.application.out.FindSwimmingPoolDetailFreeSwimmingDetailDsGateway;
import com.project.swimcb.swimmingpool.domain.FindSwimmingPoolDetailFreeSwimmingDetailCondition;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimmingDetail;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimmingDetail.FreeSwimming;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimmingDetail.Ticket;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimmingDetail.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindSwimmingPoolDetailFreeSwimmingDetailInteractorTest {

  @InjectMocks
  private FindSwimmingPoolDetailFreeSwimmingDetailInteractor interactor;

  @Mock
  private FindSwimmingPoolDetailFreeSwimmingDetailDsGateway gateway;

  @Test
  @DisplayName("조건이 유효하면 결과를 반환한다")
  void shouldReturnResultWhenConditionIsValid() {
    // given
    val condition = FindSwimmingPoolDetailFreeSwimmingDetailCondition.builder()
        .memberId(1L)
        .swimmingPoolId(1L)
        .date(LocalDate.of(2025, 7, 23))
        .build();

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
    val expectedResult = new SwimmingPoolDetailFreeSwimmingDetail(List.of(freeSwimming));

    given(gateway.findSwimmingPoolDetailFreeSwimmingDetail(condition)).willReturn(expectedResult);

    // when
    val actualResult = interactor.findSwimmingPoolDetailFreeSwimmingDetail(condition);

    // then
    assertThat(actualResult).isEqualTo(expectedResult);
    then(gateway).should().findSwimmingPoolDetailFreeSwimmingDetail(condition);
  }

  @Test
  @DisplayName("조건이 null이면 예외를 발생시킨다")
  void shouldThrowExceptionWhenConditionIsNull() {
    // when & then
    assertThatThrownBy(() -> interactor.findSwimmingPoolDetailFreeSwimmingDetail(null))
        .isInstanceOf(NullPointerException.class);
  }

}
