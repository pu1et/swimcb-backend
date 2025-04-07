package com.project.swimcb.reservation.application;

import com.project.swimcb.reservation.application.port.in.FindReservationUseCase;
import com.project.swimcb.reservation.application.port.out.FindReservationGateway;
import com.project.swimcb.reservation.domain.ReservationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindReservationInteractor implements FindReservationUseCase {

  private final FindReservationGateway gateway;

  @Override
  public ReservationInfo findReservation(long reservationId) {
    return gateway.findReservation(reservationId);
  }
}
