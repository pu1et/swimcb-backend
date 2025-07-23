package com.project.swimcb.swimmingpool.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.project.swimcb.swimmingpool.application.out.FindSwimmingPoolDetailFreeAvailableDaysDsGateway;
import com.project.swimcb.swimmingpool.domain.FindSwimmingPoolDetailFreeSwimmingAvailableDaysCondition;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeAvailableDays;
import java.time.YearMonth;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindSwimmingPoolDetailFreeAvailableDaysInteractorTest {

  @InjectMocks
  private FindSwimmingPoolDetailFreeAvailableDaysInteractor interactor;

  @Mock
  private FindSwimmingPoolDetailFreeAvailableDaysDsGateway gateway;

  @Test
  @DisplayName("조건이 유효하면 결과를 반환한다")
  void shouldReturnResultWhenConditionIsValid() {
    // given
    val condition = FindSwimmingPoolDetailFreeSwimmingAvailableDaysCondition.builder()
        .swimmingPoolId(1L)
        .month(YearMonth.of(2025, 7))
        .build();
    val expectedResult = new SwimmingPoolDetailFreeAvailableDays(List.of(1, 2, 3));

    given(gateway.findSwimmingPoolDetailFreeAvailableDays(condition)).willReturn(expectedResult);

    // when
    val actualResult = interactor.findSwimmingPoolDetailFreeSwimmingAvailableDays(condition);

    // then
    assertThat(actualResult).isEqualTo(expectedResult);
    then(gateway).should().findSwimmingPoolDetailFreeAvailableDays(condition);
  }

  @Test
  @DisplayName("조건이 null이면 예외를 발생시킨다")
  void shouldThrowExceptionWhenConditionIsNull() {
    // when & then
    assertThatThrownBy(() -> interactor.findSwimmingPoolDetailFreeSwimmingAvailableDays(null))
        .isInstanceOf(NullPointerException.class);
  }

}
