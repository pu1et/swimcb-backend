package com.project.swimcb.mypage.reservation.application.port.out;

import com.project.swimcb.mypage.reservation.domain.Reservation;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindReservationsDsGateway {

  Page<Reservation> findReservations(long memberId, @NonNull Pageable pageable);
}
