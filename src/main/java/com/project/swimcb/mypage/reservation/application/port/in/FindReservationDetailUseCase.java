package com.project.swimcb.mypage.reservation.application.port.in;

import com.project.swimcb.mypage.reservation.domain.ReservationDetail;

public interface FindReservationDetailUseCase {

  ReservationDetail findReservationDetail(long reservationId);
}
