package com.project.swimcb.bo.freeswimming.application;

import com.project.swimcb.bo.freeswimming.application.port.in.CreateBoFreeSwimmingUseCase;
import com.project.swimcb.bo.freeswimming.domain.CreateBoFreeSwimmingCommand;
import com.project.swimcb.db.entity.FreeSwimmingDayStatusEntity;
import com.project.swimcb.db.entity.FreeSwimmingEntity;
import com.project.swimcb.db.entity.SwimmingInstructorEntity;
import com.project.swimcb.db.entity.TicketEntity;
import com.project.swimcb.db.repository.FreeSwimmingDayStatusRepository;
import com.project.swimcb.db.repository.FreeSwimmingRepository;
import com.project.swimcb.db.repository.SwimmingClassTicketRepository;
import com.project.swimcb.db.repository.SwimmingInstructorRepository;
import com.project.swimcb.db.repository.SwimmingPoolRepository;
import jakarta.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
class CreateBoFreeSwimmingInteractor implements CreateBoFreeSwimmingUseCase {

  private final SwimmingPoolRepository swimmingPoolRepository;
  private final SwimmingInstructorRepository instructorRepository;
  private final FreeSwimmingRepository freeSwimmingRepository;
  private final FreeSwimmingDayStatusRepository freeSwimmingDayStatusRepository;
  private final SwimmingClassTicketRepository ticketRepository;

  @Override
  public void createBoFreeSwimming(@NonNull CreateBoFreeSwimmingCommand command) {
    val pool = swimmingPoolRepository.findById(command.swimmingPoolId())
        .orElseThrow(() -> new IllegalArgumentException("수영장이 존재하지 않습니다."));

    val lifeguardId = findLifeguardIdIfExist(command.lifeguardId());

    val freeSwimmingEntity = FreeSwimmingEntity.builder()
        .swimmingPool(pool)
        .yearMonth(command.yearMonth().atDay(1))
        .daysOfWeek(daysOfWeek(command.days()))
        .startTime(command.time().startTime())
        .endTime(command.time().endTime())
        .lifeguard(lifeguardId)
        .capacity(command.capacity())
        .isVisible(command.isExposed())
        .build();
    val savedFreeSwimming = freeSwimmingRepository.save(freeSwimmingEntity);

    val tickets = command.tickets().stream()
        .map(i -> TicketEntity.createFreeSwimmingTicket(savedFreeSwimming, i.name(), i.price()))
        .toList();
    ticketRepository.saveAll(tickets);

    val dayStatues = createDayStatuses(savedFreeSwimming, command.days());
    freeSwimmingDayStatusRepository.saveAll(dayStatues);
  }

  private List<FreeSwimmingDayStatusEntity> createDayStatuses(
      @NonNull FreeSwimmingEntity freeSwimming,
      @NonNull List<DayOfWeek> days
  ) {
    val startDayOfMonth = YearMonth.from(freeSwimming.getYearMonth()).atDay(1);
    val endDayOfMonth = YearMonth.from(freeSwimming.getYearMonth()).atEndOfMonth();

    return Stream.iterate(startDayOfMonth, date -> !date.isAfter(endDayOfMonth),
            date -> date.plusDays(1))
        .filter(date -> days.contains(date.getDayOfWeek()))
        .map(i -> FreeSwimmingDayStatusEntity.builder()
            .freeSwimming(freeSwimming)
            .dayOfMonth(i.getDayOfMonth())
            .reservedCount(0)
            .isClosed(false)
            .isReservationBlocked(false)
            .build())
        .toList();
  }

  private SwimmingInstructorEntity findLifeguardIdIfExist(Long lifeguardId) {
    if (lifeguardId == null) {
      return null;
    }

    return instructorRepository.findById(lifeguardId)
        .orElseThrow(() -> new IllegalArgumentException("안전근무 요원 정보가 존재하지 않습니다."));
  }

  private int daysOfWeek(@NonNull List<DayOfWeek> days) {
    return days.stream()
        .map(i -> 1 << (6 - (i.getValue() - 1)))
        .reduce(0, (a, b) -> a | b);
  }

}
