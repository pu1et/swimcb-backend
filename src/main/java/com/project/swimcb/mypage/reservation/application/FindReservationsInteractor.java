package com.project.swimcb.mypage.reservation.application;

import com.project.swimcb.mypage.reservation.application.port.in.FindReservationsUseCase;
import com.project.swimcb.mypage.reservation.application.port.out.FindReservationsDsGateway;
import com.project.swimcb.mypage.reservation.domain.Reservation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class FindReservationsInteractor implements FindReservationsUseCase {

  private final FindReservationsDsGateway gateway;

  @Override
  public Page<Reservation> findReservations(long memberId, @NonNull Pageable pageable) {
    return gateway.findReservations(memberId, pageable);
  }
}
