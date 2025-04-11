package com.project.swimcb.mypage.reservation.application;

import com.project.swimcb.mypage.reservation.application.port.in.FindReservationDetailUseCase;
import com.project.swimcb.mypage.reservation.application.port.out.FindReservationDetailGateway;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindReservationDetailInteractor implements FindReservationDetailUseCase {

  private final FindReservationDetailGateway gateway;

  @Override
  public ReservationDetail findReservationDetail(long reservationId) {
    return gateway.findReservationDetail(reservationId);
  }
}
