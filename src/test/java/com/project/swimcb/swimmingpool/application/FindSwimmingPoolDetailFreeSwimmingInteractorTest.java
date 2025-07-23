package com.project.swimcb.swimmingpool.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.project.swimcb.swimmingpool.application.out.FindSwimmingPoolDetailFreeSwimmingDsGateway;
import com.project.swimcb.swimmingpool.domain.FindSwimmingPoolDetailFreeSwimmingCondition;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimming;
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
class FindSwimmingPoolDetailFreeSwimmingInteractorTest {

  @InjectMocks
  private FindSwimmingPoolDetailFreeSwimmingInteractor interactor;

  @Mock
  private FindSwimmingPoolDetailFreeSwimmingDsGateway gateway;

  @Test
  @DisplayName("조건이 유효하면 결과를 반환한다")
  void shouldReturnResultWhenConditionIsValid() {
    // given
    val condition = FindSwimmingPoolDetailFreeSwimmingCondition.builder()
        .swimmingPoolId(1L)
        .month(YearMonth.of(2025, 7))
        .build();
    val expectedResult = new SwimmingPoolDetailFreeSwimming(List.of());

    given(gateway.findSwimmingPoolDetailFreeSwimming(condition)).willReturn(expectedResult);

    // when
    val actualResult = interactor.findSwimmingPoolDetailFreeSwimming(condition);

    // then
    assertThat(actualResult).isEqualTo(expectedResult);
    then(gateway).should().findSwimmingPoolDetailFreeSwimming(condition);
  }

  @Test
  @DisplayName("조건이 null이면 예외를 발생시킨다")
  void shouldThrowExceptionWhenConditionIsNull() {
    // when & then
    assertThatThrownBy(() -> interactor.findSwimmingPoolDetailFreeSwimming(null))
        .isInstanceOf(NullPointerException.class);
  }

}
