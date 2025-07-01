package com.project.swimcb.swimmingpool.application;

import static com.project.swimcb.swimmingpool.domain.SwimmingClassAvailabilityStatus.NOT_RESERVABLE;
import static com.project.swimcb.swimmingpool.domain.SwimmingClassAvailabilityStatus.WAITING_RESERVABLE;

import com.project.swimcb.db.entity.ReservationEntity;
import com.project.swimcb.db.entity.SwimmingClassEntity;
import com.project.swimcb.db.repository.SwimmingClassRepository;
import com.project.swimcb.db.entity.TicketEntity;
import com.project.swimcb.db.repository.SwimmingClassTicketRepository;
import com.project.swimcb.db.repository.MemberRepository;
import com.project.swimcb.db.entity.MemberEntity;
import com.project.swimcb.swimmingpool.application.in.ReserveSwimmingClassUseCase;
import com.project.swimcb.swimmingpool.domain.ReservationInfo;
import com.project.swimcb.db.repository.ReservationRepository;
import com.project.swimcb.swimmingpool.domain.ReserveSwimmingClassCommand;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class ReserveSwimmingClassInteractor implements ReserveSwimmingClassUseCase {

  private final SwimmingClassRepository swimmingClassRepository;
  private final ReservationRepository reservationRepository;
  private final MemberRepository memberRepository;
  private final SwimmingClassTicketRepository ticketRepository;

  @Override
  public ReservationInfo reserveSwimmingClass(@NonNull ReserveSwimmingClassCommand command) {
    val swimmingClass = swimmingClassRepository.findById(command.swimmingClassId())
        .orElseThrow(
            () -> new NoSuchElementException("수영 클래스가 존재하지 않습니다 : " + command.swimmingClassId()));

    val ticket = ticketRepository.findById(command.ticketId())
        .orElseThrow(() -> new NoSuchElementException("티켓이 존재하지 않습니다 : " + command.ticketId()));

    val member = memberRepository.findById(command.memberId())
        .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다 : " + command.memberId()));

    val reservation = createClassReservation(command, swimmingClass, ticket, member);

    val createdReservation = reservationRepository.save(reservation);

    val reservationInfo = reservationInfo(createdReservation, swimmingClass);

    swimmingClass.increaseReservedCount();

    return reservationInfo;
  }

  private ReservationEntity createClassReservation(@NonNull ReserveSwimmingClassCommand command,
      @NonNull SwimmingClassEntity swimmingClass, @NonNull TicketEntity ticket,
      @NonNull MemberEntity member) {

    val reservationStatus = swimmingClass.getReservationStatus();

    if (reservationStatus == NOT_RESERVABLE) {
      throw new IllegalStateException("대기 예약도 불가능한 상태입니다.");
    }

    if (reservationStatus == WAITING_RESERVABLE) {
      return ReservationEntity.createClassWaitingReservation()
          .member(member)
          .swimmingClass(swimmingClass)
          .ticketId(command.ticketId())
          .paymentMethod(command.paymentMethod())
          .paymentAmount(ticket.getPrice())
          .build();
    }

    // 결제대기 예약 생성
    return ReservationEntity.createClassNormalReservation()
        .member(member)
        .swimmingClass(swimmingClass)
        .ticketId(command.ticketId())
        .paymentMethod(command.paymentMethod())
        .paymentAmount(ticket.getPrice())
        .build();
  }

  private ReservationInfo reservationInfo(@NonNull ReservationEntity createdReservation,
      @NonNull SwimmingClassEntity swimmingClass) {
    if (swimmingClass.getReservationStatus() == WAITING_RESERVABLE) {
      return ReservationInfo.builder()
          .id(createdReservation.getId())
          .availabilityStatus(swimmingClass.getReservationStatus())
          .waitingNo(swimmingClass.calculateWaitingNum())
          .build();
    }

    return ReservationInfo.builder()
        .id(createdReservation.getId())
        .availabilityStatus(swimmingClass.getReservationStatus())
        .build();
  }
}
