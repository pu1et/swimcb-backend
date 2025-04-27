package com.project.swimcb.bo.reservation.application.port.in;

import com.project.swimcb.bo.reservation.domain.BoReservation;
import com.project.swimcb.bo.reservation.domain.FindBoReservationsCondition;
import lombok.NonNull;
import org.springframework.data.domain.Page;

public interface FindBoReservationsUseCase {

  Page<BoReservation> findBoReservations(@NonNull FindBoReservationsCondition condition);
}
