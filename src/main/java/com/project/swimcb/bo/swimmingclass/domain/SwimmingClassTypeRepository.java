package com.project.swimcb.bo.swimmingclass.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwimmingClassTypeRepository extends JpaRepository<SwimmingClassType, Long> {

  Optional<SwimmingClassType> findById(long swimmingClassTypeId);
}
