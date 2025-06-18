package com.project.swimcb.db.repository;

import com.project.swimcb.db.entity.SwimmingClassSubTypeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwimmingClassSubTypeRepository extends JpaRepository<SwimmingClassSubTypeEntity, Long> {

  Optional<SwimmingClassSubTypeEntity> findById(long swimmingClassSubTypeId);

  Optional<SwimmingClassSubTypeEntity> findBySwimmingClassType_IdAndId(long swimmingClassTypeId,
      long swimmingClassSubTypeId);

  int deleteBySwimmingClassType_IdAndId(long swimmingClassTypeId, long swimmingClassSubTypeId);
}
