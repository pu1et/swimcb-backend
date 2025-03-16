package com.project.swimcb.swimming_pool_review.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SwimmingPoolReviewRepository extends JpaRepository<SwimmingPoolReview, Long> {

  int countBySwimmingPool_Id(long swimmingPoolId);
}
