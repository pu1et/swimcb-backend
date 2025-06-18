package com.project.swimcb.db.repository;

import com.project.swimcb.db.entity.ReservationEntity;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

  Optional<ReservationEntity> findByIdAndMemberId(@NonNull Long reservationId, @NonNull Long memberId);
}
