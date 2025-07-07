package com.project.swimcb.bo.freeswimming.application.port;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;

import com.project.swimcb.bo.freeswimming.adapter.in.UpdateBoFreeSwimmingCommand;
import com.project.swimcb.bo.freeswimming.application.port.out.DateProvider;
import com.project.swimcb.bo.freeswimming.application.port.out.UpdateBoFreeSwimmingDsGateway;
import com.project.swimcb.bo.freeswimming.domain.DaysOfWeek;
import com.project.swimcb.db.entity.FreeSwimmingEntity;
import com.project.swimcb.db.repository.FreeSwimmingDayStatusRepository;
import com.project.swimcb.db.repository.FreeSwimmingRepository;
import com.project.swimcb.db.repository.SwimmingClassTicketRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateBoFreeSwimmingInteractorTest {

  @InjectMocks
  private UpdateBoFreeSwimmingInteractor interactor;

  @Mock
  private FreeSwimmingRepository freeSwimmingRepository;

  @Mock
  private FreeSwimmingDayStatusRepository freeSwimmingDayStatusRepository;

  @Mock
  private DateProvider dateProvider;

  @Mock
  private UpdateBoFreeSwimmingDsGateway gateway;

  @Mock
  private SwimmingClassTicketRepository ticketRepository;

  @Nested
  @DisplayName("유효한 자유수영 정보가 주어진 경우")
  class ContextWithValidFreeSwimming {

    @Test
    @DisplayName("자유수영 정보를 성공적으로 업데이트한다")
    void shouldUpdateFreeSwimmingSuccessfully() {
      // given
      val command = TestUpdateBoFreeSwimmingCommandFactory.create();
      val freeSwimming = TestFreeSwimmingEntityFactory.createWithExistingDays();
      val today = LocalDate.of(2024, 1, 15);

      given(freeSwimmingRepository.findBySwimmingPoolIdAndId(1L, 1L))
          .willReturn(Optional.of(freeSwimming));
      given(dateProvider.now()).willReturn(today);
      willDoNothing().given(freeSwimmingDayStatusRepository)
          .deleteByFreeSwimmingIdAndDayOfMonthIn(anyLong(), anyList());
      willReturn(List.of()).given(freeSwimmingDayStatusRepository)
          .saveAll(anyList());
      willDoNothing().given(gateway).updateFreeSwimming(any());
      willDoNothing().given(gateway).deleteAllTicketsByFreeSwimmingId(anyLong());
      willReturn(List.of()).given(ticketRepository).saveAll(anyList());

      // when
      interactor.updateFreeSwimmingImage(command);

      // then
      then(freeSwimmingRepository).should().findBySwimmingPoolIdAndId(1L, 1L);
      then(freeSwimmingDayStatusRepository).should()
          .deleteByFreeSwimmingIdAndDayOfMonthIn(eq(1L), anyList());
      then(freeSwimmingDayStatusRepository).should().saveAll(anyList());
      then(gateway).should().updateFreeSwimming(command);
      then(gateway).should().deleteAllTicketsByFreeSwimmingId(1L);
      then(ticketRepository).should().saveAll(any());
    }

    @Nested
    @DisplayName("요일 변경")
    class DayOfWeekTest {

      @Nested
      @DisplayName("요일 변경이 필요한 경우")
      class ContextWithDayOfWeekChanges {

        @Test
        @DisplayName("제거된 요일의 일정을 삭제하고 추가된 요일의 일정을 생성한다")
        void shouldUpdateDayStatusCorrectly() {
          // given
          val command = TestUpdateBoFreeSwimmingCommandFactory.createUpdateCommandWithNewDays(); // 월, 금
          val freeSwimming = TestFreeSwimmingEntityFactory.createWithExistingDays(); // 월, 화
          val today = LocalDate.of(2024, 1, 15); // 월요일

          given(freeSwimmingRepository.findBySwimmingPoolIdAndId(1L, 1L))
              .willReturn(Optional.of(freeSwimming));
          given(dateProvider.now()).willReturn(today);
          willDoNothing().given(gateway).updateFreeSwimming(any());
          willDoNothing().given(gateway).deleteAllTicketsByFreeSwimmingId(anyLong());

          // when
          interactor.updateFreeSwimmingImage(command);

          // then
          then(freeSwimmingDayStatusRepository).should()
              .deleteByFreeSwimmingIdAndDayOfMonthIn(eq(1L), anyList());
          then(freeSwimmingDayStatusRepository).should().saveAll(anyList());
        }

      }

      @Nested
      @DisplayName("요일 변경이 없는 경우")
      class ContextWithoutDayOfWeekChanges {

        @Test
        @DisplayName("요일 관련 삭제/추가 작업을 수행하지 않는다")
        void shouldNotUpdateDayStatusWhenNoChanges() {
          // given
          val command = TestUpdateBoFreeSwimmingCommandFactory.createWithSameDays();
          val freeSwimming = TestFreeSwimmingEntityFactory.createWithSameDays();

          given(freeSwimmingRepository.findBySwimmingPoolIdAndId(1L, 1L))
              .willReturn(Optional.of(freeSwimming));
          willDoNothing().given(gateway).updateFreeSwimming(any());
          willDoNothing().given(gateway).deleteAllTicketsByFreeSwimmingId(anyLong());

          // when
          interactor.updateFreeSwimmingImage(command);

          // then
          then(freeSwimmingDayStatusRepository).shouldHaveNoInteractions();
        }

      }

    }

  }

  @Nested
  @DisplayName("존재하지 않는 자유수영 ID가 주어진 경우")
  class ContextWithNonExistentFreeSwimming {

    @Test
    @DisplayName("NoSuchElementException을 발생시킨다")
    void shouldThrowNoSuchElementException() {
      // given
      val command = TestUpdateBoFreeSwimmingCommandFactory.create();

      given(freeSwimmingRepository.findBySwimmingPoolIdAndId(1L, 1L))
          .willReturn(Optional.empty());

      // when
      // then
      assertThatThrownBy(() -> interactor.updateFreeSwimmingImage(command))
          .isInstanceOf(NoSuchElementException.class)
          .hasMessage("자유수영이 존재하지 않습니다.");
    }

  }

  private static class TestUpdateBoFreeSwimmingCommandFactory {

    private static UpdateBoFreeSwimmingCommand.UpdateBoFreeSwimmingCommandBuilder createCommon() {
      return UpdateBoFreeSwimmingCommand.builder()
          .swimmingPoolId(1L)
          .freeSwimmingId(1L)
          .daysOfWeek(new DaysOfWeek(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)))
          .time(
              UpdateBoFreeSwimmingCommand.Time.builder()
                  .startTime(LocalTime.of(9, 0))
                  .endTime(LocalTime.of(10, 0))
                  .build()
          )
          .lifeguardId(1L)
          .tickets(List.of(
              new UpdateBoFreeSwimmingCommand.Ticket("성인일반", 10000)
          ))
          .capacity(20)
          .isExposed(true);
    }

    private static UpdateBoFreeSwimmingCommand create() {
      return createCommon().build();
    }

    private static UpdateBoFreeSwimmingCommand createWithSameDays() {
      return createCommon()
          .daysOfWeek(new DaysOfWeek(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)))
          .build();
    }

    private static UpdateBoFreeSwimmingCommand createUpdateCommandWithNewDays() {
      return createCommon()
          .daysOfWeek(new DaysOfWeek(List.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)))
          .build();
    }

  }

  private static class TestFreeSwimmingEntityFactory {

    private static FreeSwimmingEntity.FreeSwimmingEntityBuilder createCommon() {
      return FreeSwimmingEntity.builder()
          .id(1L)
          .daysOfWeek(new DaysOfWeek(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)).toInt())
          .startTime(LocalTime.of(9, 0))
          .endTime(LocalTime.of(10, 0))
          .capacity(20)
          .isVisible(true);
    }

    private static FreeSwimmingEntity create() {
      return createCommon().build();
    }

    private static FreeSwimmingEntity createWithExistingDays() {
      return createCommon()
          .daysOfWeek(new DaysOfWeek(List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY)).toInt())
          .build();
    }

    private static FreeSwimmingEntity createWithSameDays() {
      return createCommon()
          .daysOfWeek(new DaysOfWeek(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)).toInt())
          .build();
    }

  }

}
