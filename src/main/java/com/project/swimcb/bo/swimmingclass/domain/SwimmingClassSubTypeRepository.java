package com.project.swimcb.bo.swimmingclass.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwimmingClassSubTypeRepository extends JpaRepository<SwimmingClassSubType, Long> {

  Optional<SwimmingClassSubType> findById(long swimmingClassSubTypeId);

  Optional<SwimmingClassSubType> findBySwimmingClassType_IdAndId(long swimmingClassTypeId,
      long swimmingClassSubTypeId);

  int deleteBySwimmingClassType_IdAndId(long swimmingClassTypeId, long swimmingClassSubTypeId);
}
