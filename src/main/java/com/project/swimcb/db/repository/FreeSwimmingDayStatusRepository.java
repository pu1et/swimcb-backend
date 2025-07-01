package com.project.swimcb.db.repository;

import com.project.swimcb.db.entity.FreeSwimmingDayStatusEntity;
import com.project.swimcb.db.entity.FreeSwimmingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeSwimmingDayStatusRepository extends
    JpaRepository<FreeSwimmingDayStatusEntity, Long> {

}
