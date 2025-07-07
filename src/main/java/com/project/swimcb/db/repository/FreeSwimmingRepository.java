package com.project.swimcb.db.repository;

import com.project.swimcb.db.entity.FreeSwimmingEntity;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeSwimmingRepository extends JpaRepository<FreeSwimmingEntity, Long> {

  Optional<FreeSwimmingEntity> findBySwimmingPoolIdAndId(
      @NonNull Long swimmingPoolId,
      @NonNull Long freeSwimmingId
  );
}
