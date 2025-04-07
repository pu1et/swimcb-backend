package com.project.swimcb.reservation.application.port.in;

import com.project.swimcb.reservation.domain.ReservationInfo;

public interface FindReservationUseCase {

  ReservationInfo findReservation(long reservationId);
}
