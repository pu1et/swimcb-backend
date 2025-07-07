package com.project.swimcb.db.repository;

import com.project.swimcb.db.entity.FreeSwimmingDayStatusEntity;
import java.util.List;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeSwimmingDayStatusRepository extends
    JpaRepository<FreeSwimmingDayStatusEntity, Long> {

  void deleteByFreeSwimmingIdAndDayOfMonthIn(
      @NonNull Long freeSwimmingId,
      @NonNull List<Integer> dayOfMonths
  );
}
