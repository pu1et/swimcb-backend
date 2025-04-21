package com.project.swimcb.mypage.reservation.application.port.in;

import com.project.swimcb.mypage.reservation.domain.Reservation;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindReservationsUseCase {

  Page<Reservation> findReservations(long memberId, @NonNull Pageable pageable);
}
