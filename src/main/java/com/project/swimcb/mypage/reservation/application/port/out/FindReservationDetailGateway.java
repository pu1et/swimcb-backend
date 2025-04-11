package com.project.swimcb.mypage.reservation.application.port.out;

import com.project.swimcb.mypage.reservation.domain.ReservationDetail;

public interface FindReservationDetailGateway {

  ReservationDetail findReservationDetail(long reservationId);
}
