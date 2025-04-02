package com.project.swimcb.swimmingpool.application;

import static com.project.swimcb.swimmingpool.domain.SwimmingClassAvailabilityStatus.NOT_RESERVABLE;
import static com.project.swimcb.swimmingpool.domain.SwimmingClassAvailabilityStatus.WAITING_RESERVABLE;

import com.project.swimcb.bo.swimmingclass.domain.SwimmingClass;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassRepository;
import com.project.swimcb.member.MemberRepository;
import com.project.swimcb.member.domain.Member;
import com.project.swimcb.swimmingpool.application.in.ReserveSwimmingClassUseCase;
import com.project.swimcb.swimmingpool.domain.Reservation;
import com.project.swimcb.swimmingpool.domain.ReservationInfo;
import com.project.swimcb.swimmingpool.domain.ReservationRepository;
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

  @Override
  public ReservationInfo reserveSwimmingClass(@NonNull ReserveSwimmingClassCommand command) {
    val swimmingClass = swimmingClassRepository.findById(command.swimmingClassId())
        .orElseThrow(
            () -> new NoSuchElementException("수영 클래스가 존재하지 않습니다 : " + command.swimmingClassId()));

    val member = memberRepository.findById(command.memberId())
        .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다 : " + command.memberId()));

    val reservation = createClassReservation(command, swimmingClass, member);

    val createdReservation = reservationRepository.save(reservation);

    swimmingClass.increaseReservedCount();

    return reservationInfo(createdReservation, swimmingClass);
  }

  private Reservation createClassReservation(@NonNull ReserveSwimmingClassCommand command,
      @NonNull SwimmingClass swimmingClass, @NonNull Member member) {

    val reservationStatus = swimmingClass.getReservationStatus();

    if (reservationStatus == NOT_RESERVABLE) {
      throw new IllegalStateException("대기 예약도 불가능한 상태입니다.");
    }

    if (reservationStatus == WAITING_RESERVABLE) {
      return Reservation.createClassWaitingReservation()
          .member(member)
          .ticketId(command.ticketId())
          .paymentMethod(command.paymentMethod())
          .waitingNo(swimmingClass.calculateWaitingNum())
          .build();
    }

    return Reservation.createClassNormalReservation()
        .member(member)
        .ticketId(command.ticketId())
        .paymentMethod(command.paymentMethod())
        .build();
  }

  private ReservationInfo reservationInfo(@NonNull Reservation createdReservation, @NonNull SwimmingClass swimmingClass) {
    return ReservationInfo.builder()
        .id(createdReservation.getId())
        .availabilityStatus(swimmingClass.getReservationStatus())
        .waitingNo(createdReservation.getWaitingNo())
        .build();
  }
}
