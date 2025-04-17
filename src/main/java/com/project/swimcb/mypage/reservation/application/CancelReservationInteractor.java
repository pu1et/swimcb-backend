package com.project.swimcb.mypage.reservation.application;

import com.project.swimcb.mypage.reservation.application.port.in.CancelReservationUseCase;
import com.project.swimcb.swimmingpool.domain.ReservationRepository;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CancelReservationInteractor implements CancelReservationUseCase {

  private final ReservationRepository reservationRepository;

  @Override
  public void cancelReservation(@NonNull Long memberId, @NonNull Long reservationId) {
    val reservation = reservationRepository.findByIdAndMemberId(reservationId, memberId)
        .orElseThrow(() -> new NoSuchElementException("예약을 찾을 수 없습니다 : " + reservationId));

    reservation.cancel();
  }
}
