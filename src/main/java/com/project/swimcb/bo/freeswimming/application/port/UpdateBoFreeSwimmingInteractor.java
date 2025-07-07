package com.project.swimcb.bo.freeswimming.application.port;

import com.project.swimcb.bo.freeswimming.adapter.in.UpdateBoFreeSwimmingCommand;
import com.project.swimcb.bo.freeswimming.adapter.in.UpdateBoFreeSwimmingCommand.Ticket;
import com.project.swimcb.bo.freeswimming.application.port.in.UpdateBoFreeSwimmingUseCase;
import com.project.swimcb.bo.freeswimming.application.port.out.DateProvider;
import com.project.swimcb.bo.freeswimming.application.port.out.UpdateBoFreeSwimmingDsGateway;
import com.project.swimcb.bo.freeswimming.domain.DaysOfWeek;
import com.project.swimcb.db.entity.FreeSwimmingDayStatusEntity;
import com.project.swimcb.db.entity.FreeSwimmingEntity;
import com.project.swimcb.db.entity.TicketEntity;
import com.project.swimcb.db.repository.FreeSwimmingDayStatusRepository;
import com.project.swimcb.db.repository.FreeSwimmingRepository;
import com.project.swimcb.db.repository.SwimmingClassTicketRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class UpdateBoFreeSwimmingInteractor implements UpdateBoFreeSwimmingUseCase {

  private final FreeSwimmingRepository freeSwimmingRepository;
  private final FreeSwimmingDayStatusRepository freeSwimmingDayStatusRepository;
  private final DateProvider dateProvider;
  private final UpdateBoFreeSwimmingDsGateway gateway;
  private final SwimmingClassTicketRepository ticketRepository;

  @Override
  public void updateFreeSwimmingImage(@NonNull UpdateBoFreeSwimmingCommand command) {
    val freeSwimming = findBySwimmingPoolIdAndId(command.swimmingPoolId(),
        command.freeSwimmingId());

    updateFreeSwimmingDayStatus(freeSwimming, command.daysOfWeek());

    gateway.updateFreeSwimming(command);

    updateTickets(freeSwimming, command.tickets());
  }

  private FreeSwimmingEntity findBySwimmingPoolIdAndId(
      @NonNull Long swimmingPoolId,
      @NonNull Long freeSwimmingId
  ) {
    return freeSwimmingRepository.findBySwimmingPoolIdAndId(swimmingPoolId, freeSwimmingId)
        .orElseThrow(() -> new NoSuchElementException("자유수영이 존재하지 않습니다."));
  }

  private void updateFreeSwimmingDayStatus(
      @NonNull FreeSwimmingEntity freeSwimming,
      @NonNull DaysOfWeek newDaysOfWeek
  ) {
    val currentDaysOfWeek = DaysOfWeek.of(freeSwimming.getDaysOfWeek());

    if (currentDaysOfWeek.equals(newDaysOfWeek)) {
      return;
    }

    // 현재 자유수영의 요일과 새로 설정된 요일을 비교하여 제거할 요일과 추가할 요일을 구분합니다.
    removeDaysOfWeek(currentDaysOfWeek, newDaysOfWeek, freeSwimming.getId());
    addDaysOfWeek(currentDaysOfWeek, newDaysOfWeek, freeSwimming);
  }

  /**
   * 현재 날짜부터 해당 월의 마지막 날까지 반복하면서, 추가할 요일에 해당하는 날짜를 찾아 추가합니다.
   */
  private void addDaysOfWeek(
      @NonNull DaysOfWeek currentDaysOfWeek,
      @NonNull DaysOfWeek newDaysOfWeek,
      @NonNull FreeSwimmingEntity freeSwimming
  ) {

    val addDaysOfWeek = newDaysOfWeek.value()
        .stream()
        .filter(i -> !currentDaysOfWeek.value().contains(i))
        .toList();

    val today = dateProvider.now();
    val endOfMonth = YearMonth.from(today).atEndOfMonth();

    val addDays = Stream.iterate(today, date -> !date.isAfter(endOfMonth),
            date -> date.plusDays(1))
        .filter(date -> addDaysOfWeek.contains(date.getDayOfWeek()))
        .map(i -> FreeSwimmingDayStatusEntity.builder()
            .freeSwimming(freeSwimming)
            .dayOfMonth(i.getDayOfMonth())
            .reservedCount(0)
            .isClosed(false)
            .isReservationBlocked(false)
            .build())
        .toList();

    if (addDays.isEmpty()) {
      return;
    }

    freeSwimmingDayStatusRepository.saveAll(addDays);
  }

  /**
   * 현재 날짜부터 해당 월의 마지막 날까지 반복하면서, 제거할 요일에 해당하는 날짜를 찾아 삭제합니다.
   */
  private void removeDaysOfWeek(
      @NonNull DaysOfWeek currentDaysOfWeek,
      @NonNull DaysOfWeek newDaysOfWeek,
      @NonNull Long freeSwimmingId
  ) {
    val removeDaysOfWeek = currentDaysOfWeek.value()
        .stream()
        .filter(i -> !newDaysOfWeek.value().contains(i))
        .toList();

    val today = dateProvider.now();
    val endOfMonth = YearMonth.from(today).atEndOfMonth();

    val removeDays = Stream.iterate(today, date -> !date.isAfter(endOfMonth),
            date -> date.plusDays(1))
        .filter(date -> removeDaysOfWeek.contains(date.getDayOfWeek()))
        .map(LocalDate::getDayOfMonth)
        .toList();

    if (removeDays.isEmpty()) {
      return;
    }
    freeSwimmingDayStatusRepository.deleteByFreeSwimmingIdAndDayOfMonthIn(freeSwimmingId,
        removeDays);
  }

  private void updateTickets(
      @NonNull FreeSwimmingEntity freeSwimmingEntity,
      @NonNull List<Ticket> tickets
  ) {

    gateway.deleteAllTicketsByFreeSwimmingId(freeSwimmingEntity.getId());

    val newTickets = tickets.stream()
        .map(i -> TicketEntity.createFreeSwimmingTicket(freeSwimmingEntity, i.name(), i.price()))
        .toList();
    ticketRepository.saveAll(newTickets);
  }

}
