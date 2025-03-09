package com.project.swimcb.bo.swimmingclass.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SwimmingClassRepository extends JpaRepository<SwimmingClass, Long> {

  int deleteBySwimmingPool_IdAndId(long swimmingPoolId, long swimmingClassId);
}
