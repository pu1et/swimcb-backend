package com.project.swimcb.reservation.application.port.out;

import com.project.swimcb.reservation.domain.ReservationInfo;

public interface FindReservationGateway {

  ReservationInfo findReservation(long reservationId);
}
