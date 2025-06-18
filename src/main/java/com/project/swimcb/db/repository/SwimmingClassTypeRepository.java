package com.project.swimcb.db.repository;

import com.project.swimcb.db.entity.SwimmingClassTypeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwimmingClassTypeRepository extends JpaRepository<SwimmingClassTypeEntity, Long> {

  Optional<SwimmingClassTypeEntity> findById(long swimmingClassTypeId);
}
