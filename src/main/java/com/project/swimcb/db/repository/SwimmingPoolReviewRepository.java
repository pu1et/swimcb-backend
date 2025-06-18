package com.project.swimcb.db.repository;

import com.project.swimcb.db.entity.SwimmingPoolReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwimmingPoolReviewRepository extends JpaRepository<SwimmingPoolReviewEntity, Long> {

  int countBySwimmingPool_Id(long swimmingPoolId);
}
