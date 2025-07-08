package com.project.swimcb.bo.freeswimming.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.project.swimcb.bo.freeswimming.application.port.out.SetFreeSwimmingClosedDsGateway;
import com.project.swimcb.bo.freeswimming.domain.SetFreeSwimmingClosedCommand;
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
class SetFreeSwimmingClosedInteractorTest {

  @InjectMocks
  private SetFreeSwimmingClosedInteractor interactor;

  @Mock
  private SetFreeSwimmingClosedDsGateway gateway;

  @Nested
  @DisplayName("자유수영 휴무일 설정 시")
  class DescribeSetFreeSwimmingClosed {

    @Nested
    @DisplayName("모든 자유수영 일별 상태가 존재하는 경우")
    class ContextWithExistingFreeSwimmingDayStatuses {

      @Test
      @DisplayName("휴무일 설정에 성공한다")
      void shouldSetFreeSwimmingClosedSuccessfully() {
        // given
        val command = SetFreeSwimmingClosedCommand.builder()
            .swimmingPoolId(1L)
            .freeSwimmingDayStatusIds(List.of(1L, 2L, 3L))
            .isClosed(true)
            .build();

        given(gateway.existsAllByIdInAndSwimmingPoolId(
            command.freeSwimmingDayStatusIds(),
            command.swimmingPoolId()
        )).willReturn(true);
        willDoNothing().given(gateway).setFreeSwimmingClosed(command);

        // when
        interactor.setFreeSwimmingClosed(command);

        // then
        then(gateway).should().existsAllByIdInAndSwimmingPoolId(
            command.freeSwimmingDayStatusIds(),
            command.swimmingPoolId()
        );
        then(gateway).should().setFreeSwimmingClosed(command);
      }

    }

    @Nested
    @DisplayName("일부 자유수영 일별 상태가 존재하지 않는 경우")
    class ContextWithNonExistentFreeSwimmingDayStatuses {

      @Test
      @DisplayName("예외를 발생시킨다")
      void shouldThrowExceptionWhenFreeSwimmingDayStatusNotFound() {
        // given
        val command = SetFreeSwimmingClosedCommand.builder()
            .swimmingPoolId(1L)
            .freeSwimmingDayStatusIds(List.of(1L, 2L, 999L))
            .isClosed(true)
            .build();

        given(gateway.existsAllByIdInAndSwimmingPoolId(
            command.freeSwimmingDayStatusIds(),
            command.swimmingPoolId()
        )).willReturn(false);

        // when
        // then
        assertThatThrownBy(() -> interactor.setFreeSwimmingClosed(command))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("자유수영 일별 리스트가 존재하지 않습니다.");

        then(gateway).should().existsAllByIdInAndSwimmingPoolId(
            command.freeSwimmingDayStatusIds(),
            command.swimmingPoolId()
        );
        then(gateway).shouldHaveNoMoreInteractions();
      }

    }

  }

}
