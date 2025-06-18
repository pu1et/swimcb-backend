package com.project.swimcb.db.repository;

import com.project.swimcb.db.entity.SwimmingClassEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwimmingClassRepository extends JpaRepository<SwimmingClassEntity, Long> {

  Optional<SwimmingClassEntity> findBySwimmingPool_IdAndId(long swimmingPoolId, long swimmingClassId);
}
