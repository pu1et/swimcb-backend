package com.project.swimcb.swimmingpool.application;

import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassRepository;
import com.project.swimcb.member.MemberRepository;
import com.project.swimcb.swimmingpool.application.in.ReserveSwimmingClassUseCase;
import com.project.swimcb.swimmingpool.domain.Reservation;
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
  public void reserveSwimmingClass(@NonNull ReserveSwimmingClassCommand command) {
    val swimmingClass = swimmingClassRepository.findById(command.swimmingClassId())
        .orElseThrow(
            () -> new NoSuchElementException("수영 클래스가 존재하지 않습니다 : " + command.swimmingClassId()));

    if (swimmingClass.isFull()) {
      throw new NoSuchElementException("예약 정원을 초과하였습니다. : " + command.swimmingClassId());
    }

    val member = memberRepository.findById(command.memberId())
        .orElseThrow(() -> new NoSuchElementException("회원이 존재하지 않습니다 : " + command.memberId()));

    val reservation = Reservation.createClassReservation()
        .member(member)
        .ticketId(command.ticketId())
        .paymentMethod(command.paymentMethod())
        .build();

    reservationRepository.save(reservation);

    swimmingClass.increaseReservedCount();
  }
}
