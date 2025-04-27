package com.project.swimcb.bo.reservation.application;

import com.project.swimcb.bo.reservation.application.port.in.FindBoReservationsUseCase;
import com.project.swimcb.bo.reservation.application.port.out.FindBoReservationsDsGateway;
import com.project.swimcb.bo.reservation.domain.BoReservation;
import com.project.swimcb.bo.reservation.domain.FindBoReservationsCondition;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class FindBoReservationsInteractor implements FindBoReservationsUseCase {

  private final FindBoReservationsDsGateway gateway;

  @Override
  public Page<BoReservation> findBoReservations(@NonNull FindBoReservationsCondition condition) {
    return gateway.findBoReservations(condition);
  }
}
