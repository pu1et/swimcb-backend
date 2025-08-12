package com.project.swimcb.bo.freeswimming.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.freeswimming.domain.CreateBoFreeSwimmingCommand;
import com.project.swimcb.db.entity.FreeSwimmingDayStatusEntity;
import com.project.swimcb.db.entity.FreeSwimmingEntity;
import com.project.swimcb.db.entity.SwimmingInstructorEntity;
import com.project.swimcb.db.entity.SwimmingPoolEntity;
import com.project.swimcb.db.entity.TicketEntity;
import com.project.swimcb.db.repository.FreeSwimmingDayStatusRepository;
import com.project.swimcb.db.repository.FreeSwimmingRepository;
import com.project.swimcb.db.repository.SwimmingClassTicketRepository;
import com.project.swimcb.db.repository.SwimmingInstructorRepository;
import com.project.swimcb.db.repository.SwimmingPoolRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
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
class CreateBoFreeSwimmingInteractorTest {

  @InjectMocks
  private CreateBoFreeSwimmingInteractor interactor;

  @Mock
  private SwimmingPoolRepository swimmingPoolRepository;

  @Mock
  private SwimmingInstructorRepository instructorRepository;

  @Mock
  private FreeSwimmingRepository freeSwimmingRepository;

  @Mock
  private FreeSwimmingDayStatusRepository freeSwimmingDayStatusRepository;

  @Mock
  private SwimmingClassTicketRepository ticketRepository;

  @Nested
  @DisplayName("자유수영 등록 시")
  class CreateFreeSwimming {

    @Test
    @DisplayName("올바른 정보로 자유수영을 등록하면 성공한다")
    void shouldCreateFreeSwimmingWhenValidCommandIsProvided() {
      // given
      val poolId = 1L;
      val lifeguardId = Long.valueOf(2L);
      val yearMonth = YearMonth.of(2025, 6);
      val days = List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
      val startTime = LocalTime.of(9, 0);
      val endTime = LocalTime.of(10, 0);
      val capacity = 20;
      val isExposed = true;

      val pool = mock(SwimmingPoolEntity.class);
      val lifeguard = mock(SwimmingInstructorEntity.class);
      val savedFreeSwimming = mock(FreeSwimmingEntity.class);
      when(savedFreeSwimming.getYearMonth()).thenReturn(yearMonth.atDay(1));

      val timeInfo = new CreateBoFreeSwimmingCommand.Time(startTime, endTime);
      val ticketInfo = new CreateBoFreeSwimmingCommand.Ticket("일반 티켓", 10000);

      val command = CreateBoFreeSwimmingCommand.builder()
          .swimmingPoolId(poolId)
          .lifeguardId(lifeguardId)
          .yearMonth(yearMonth)
          .days(days)
          .time(timeInfo)
          .capacity(capacity)
          .isExposed(isExposed)
          .tickets(List.of(ticketInfo))
          .build();

      when(swimmingPoolRepository.findById(poolId)).thenReturn(Optional.of(pool));
      when(instructorRepository.findById(lifeguardId)).thenReturn(Optional.of(lifeguard));
      when(freeSwimmingRepository.save(any(FreeSwimmingEntity.class))).thenReturn(
          savedFreeSwimming);

      // when
      interactor.createBoFreeSwimming(command);

      // then
      verify(swimmingPoolRepository, only()).findById(poolId);
      verify(instructorRepository, only()).findById(lifeguardId);
      verify(freeSwimmingRepository, only()).save(assertArg(i -> {
        assertThat(i.getSwimmingPool()).isEqualTo(pool);
        assertThat(i.getYearMonth().toString()).isEqualTo("2025-06-01");
        assertThat(i.getDaysOfWeek()).isEqualTo(0b1010100);
        assertThat(i.getStartTime()).isEqualTo(startTime);
        assertThat(i.getEndTime()).isEqualTo(endTime);
        assertThat(i.getLifeguard()).isEqualTo(lifeguard);
        assertThat(i.getCapacity()).isEqualTo(capacity);
        assertThat(i.isVisible()).isEqualTo(isExposed);
      }));
      verify(ticketRepository, only()).saveAll(assertArg(i -> {
        assertThat(i).hasSize(1);
        assertThat(i).extracting(TicketEntity::getTargetId)
            .containsExactly(savedFreeSwimming.getId());
        assertThat(i).extracting(TicketEntity::getName).containsExactly("일반 티켓");
        assertThat(i).extracting(TicketEntity::getPrice).containsExactly(10000);
      }));
      verify(freeSwimmingDayStatusRepository, only()).saveAll(anyList());
    }

    @Test
    @DisplayName("존재하지 않는 수영장 ID로 등록하면 예외가 발생한다")
    void shouldThrowExceptionWhenSwimmingPoolDoesNotExist() {
      // given
      val poolId = 999L;
      val yearMonth = YearMonth.of(2025, 6);
      val days = List.of(DayOfWeek.MONDAY);
      val time = new CreateBoFreeSwimmingCommand.Time(LocalTime.of(9, 0), LocalTime.of(10, 0));

      val command = CreateBoFreeSwimmingCommand.builder()
          .swimmingPoolId(poolId)
          .lifeguardId(null)
          .yearMonth(yearMonth)
          .days(days)
          .time(time)
          .capacity(20)
          .isExposed(true)
          .tickets(List.of())
          .build();

      when(swimmingPoolRepository.findById(poolId)).thenReturn(Optional.empty());

      // when
      // then
      assertThatThrownBy(() -> interactor.createBoFreeSwimming(command))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("수영장이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 강사 ID로 등록하면 예외가 발생한다")
    void shouldThrowExceptionWhenLifeguardDoesNotExist() {
      // given
      val poolId = 1L;
      val nonExistentLifeguardId = 999L;
      val yearMonth = YearMonth.of(2025, 6);
      val days = List.of(DayOfWeek.MONDAY);
      val time = new CreateBoFreeSwimmingCommand.Time(LocalTime.of(9, 0), LocalTime.of(10, 0));

      val command = CreateBoFreeSwimmingCommand.builder()
          .swimmingPoolId(poolId)
          .lifeguardId(nonExistentLifeguardId)
          .yearMonth(yearMonth)
          .days(days)
          .time(time)
          .capacity(20)
          .isExposed(true)
          .tickets(List.of())
          .build();

      val pool = mock(SwimmingPoolEntity.class);
      when(swimmingPoolRepository.findById(poolId)).thenReturn(Optional.of(pool));

      // when
      // then
      assertThatThrownBy(() -> interactor.createBoFreeSwimming(command))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("안전근무 요원 정보가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("강사 ID가 null일 경우 정상적으로 등록된다")
    void shouldCreateFreeSwimmingWhenLifeguardIdIsNull() {
      // given
      val poolId = 1L;
      val yearMonth = YearMonth.of(2025, 6);
      val days = List.of(DayOfWeek.MONDAY);
      val time = new CreateBoFreeSwimmingCommand.Time(LocalTime.of(9, 0), LocalTime.of(10, 0));

      val command = CreateBoFreeSwimmingCommand.builder()
          .swimmingPoolId(poolId)
          .lifeguardId(null) // lifeguardId is null
          .yearMonth(yearMonth)
          .days(days)
          .time(time)
          .capacity(20)
          .isExposed(true)
          .tickets(List.of())
          .build();

      val pool = mock(SwimmingPoolEntity.class);
      val savedFreeSwimming = mock(FreeSwimmingEntity.class);
      when(savedFreeSwimming.getYearMonth()).thenReturn(yearMonth.atDay(1));

      when(swimmingPoolRepository.findById(poolId)).thenReturn(Optional.of(pool));
      when(freeSwimmingRepository.save(any())).thenReturn(savedFreeSwimming);

      // when
      interactor.createBoFreeSwimming(command);

      // then
      verify(freeSwimmingRepository, only()).save(any(FreeSwimmingEntity.class));
      verify(instructorRepository, never()).findById(any());
    }

    @Test
    @DisplayName("요일에 맞는 DayStatus가 생성된다")
    void shouldCreateCorrectDayStatuses() {
      // given
      val poolId = 1L;
      val yearMonth = YearMonth.of(2025, 6);
      val days = List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY);
      val timeInfo = new CreateBoFreeSwimmingCommand.Time(LocalTime.of(9, 0),
          LocalTime.of(10, 0));

      val command = CreateBoFreeSwimmingCommand.builder()
          .swimmingPoolId(poolId)
          .yearMonth(yearMonth)
          .days(days)
          .time(timeInfo)
          .lifeguardId(null)
          .capacity(0)
          .isExposed(false)
          .tickets(List.of())
          .build();

      val pool = mock(SwimmingPoolEntity.class);
      val savedFreeSwimming = mock(FreeSwimmingEntity.class);
      when(savedFreeSwimming.getYearMonth()).thenReturn(yearMonth.atDay(1));

      when(swimmingPoolRepository.findById(poolId)).thenReturn(Optional.of(pool));
      when(freeSwimmingRepository.save(any())).thenReturn(savedFreeSwimming);

      // when
      interactor.createBoFreeSwimming(command);

      // then
      verify(freeSwimmingDayStatusRepository).saveAll(assertArg(i -> {
        assertThat(i).hasSize(9);
        assertThat(i).extracting(FreeSwimmingDayStatusEntity::getDayOfMonth)
            .containsExactly(2, 4, 9, 11, 16, 18, 23, 25, 30);
      }));

    }

  }

}
