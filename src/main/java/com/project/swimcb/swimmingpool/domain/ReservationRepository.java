package com.project.swimcb.swimmingpool.domain;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  Optional<Reservation> findByIdAndMemberId(@NonNull Long reservationId, @NonNull Long memberId);
}
