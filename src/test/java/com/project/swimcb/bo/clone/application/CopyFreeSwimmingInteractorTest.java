package com.project.swimcb.bo.clone.application;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;

import com.project.swimcb.bo.clone.adapter.out.FreeSwimmingCopyCandidate;
import com.project.swimcb.bo.clone.application.port.out.CopyFreeSwimmingDsGateway;
import com.project.swimcb.bo.clone.domain.CopyFreeSwimmingCommand;
import com.project.swimcb.bo.freeswimming.application.port.in.CreateBoFreeSwimmingUseCase;
import com.project.swimcb.bo.freeswimming.domain.DaysOfWeek;
import java.time.LocalTime;
import java.time.YearMonth;
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
class CopyFreeSwimmingInteractorTest {

  @InjectMocks
  private CopyFreeSwimmingInteractor interactor;

  @Mock
  private CopyFreeSwimmingDsGateway gateway;

  @Mock
  private CreateBoFreeSwimmingUseCase createBoFreeSwimmingUseCase;

  @Nested
  @DisplayName("자유수영 복사 시")
  class CopyFreeSwimmingTests {

    @Test
    @DisplayName("유효한 명령으로 자유수영 복사를 성공적으로 수행한다")
    void shouldCopyFreeSwimmingSuccessfully() {
      // given
      val command = TestCopyFreeSwimmingCommandFactory.create();
      val candidates = TestFreeSwimmingCopyCandidateFactory.createList();

      given(gateway.findAllFreeSwimmingsByMonth(command.fromMonth()))
          .willReturn(candidates);

      // when
      interactor.copyFreeSwimming(command);

      // then
      then(gateway).should(only()).findAllFreeSwimmingsByMonth(command.fromMonth());
      then(createBoFreeSwimmingUseCase)
          .should(times(2))
          .createBoFreeSwimming(assertArg(i -> {
            assertThat(i.swimmingPoolId()).isIn(1L, 2L);
            assertThat(i.yearMonth()).isEqualTo(command.toMonth());
            assertThat(i.days()).isEqualTo(List.of(MONDAY, SUNDAY));
            assertThat(i.time().startTime()).isEqualTo(LocalTime.of(9, 0));
            assertThat(i.time().endTime()).isEqualTo(LocalTime.of(10, 0));
            assertThat(i.lifeguardId()).isEqualTo(4L);
            assertThat(i.tickets()).hasSize(1);
            assertThat(i.tickets().getFirst().name()).isEqualTo("MOCK_TICKET");
            assertThat(i.tickets().getFirst().price()).isEqualTo(30000);
            assertThat(i.capacity()).isEqualTo(20);
            assertThat(i.isExposed()).isTrue();
          }));
    }

    @Test
    @DisplayName("복사 자유수영 후보 리스트가 빈 리스트이면 이후 로직을 수행하지 않는다")
    void shouldHandleEmptyCandidateList() {
      // given
      val command = TestCopyFreeSwimmingCommandFactory.create();
      val emptyCandidates = List.<FreeSwimmingCopyCandidate>of();

      given(gateway.findAllFreeSwimmingsByMonth(command.fromMonth()))
          .willReturn(emptyCandidates);

      // when
      interactor.copyFreeSwimming(command);

      // then
      then(gateway).should(only()).findAllFreeSwimmingsByMonth(command.fromMonth());
      then(createBoFreeSwimmingUseCase).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("null 파라미터가 전달되면 NullPointerException을 던진다")
    void shouldThrowExceptionWhenCommandIsNull() {
      // when
      // then
      assertThatThrownBy(() -> interactor.copyFreeSwimming(null))
          .isInstanceOf(NullPointerException.class);
    }

  }

  private static class TestCopyFreeSwimmingCommandFactory {

    private static CopyFreeSwimmingCommand create() {
      return new CopyFreeSwimmingCommand(
          YearMonth.of(2025, 1),
          YearMonth.of(2025, 2)
      );
    }

  }

  private static class TestFreeSwimmingCopyCandidateFactory {

    private static List<FreeSwimmingCopyCandidate> createList() {
      return List.of(
          createCandidate(1L),
          createCandidate(2L)
      );
    }

    private static FreeSwimmingCopyCandidate createCandidate(long swimmingPoolId) {
      return FreeSwimmingCopyCandidate.builder()
          .swimmingPoolId(swimmingPoolId)
          .days(DaysOfWeek.of(0b1000001)) // Monday and Sunday
          .time(FreeSwimmingCopyCandidate.Time.builder()
              .startTime(LocalTime.of(9, 0))
              .endTime(LocalTime.of(10, 0))
              .build())
          .lifeguardId(4L)
          .tickets(List.of(
              FreeSwimmingCopyCandidate.Ticket.builder()
                  .name("MOCK_TICKET")
                  .price(30000)
                  .build()
          ))
          .capacity(20)
          .build();
    }

  }

}
