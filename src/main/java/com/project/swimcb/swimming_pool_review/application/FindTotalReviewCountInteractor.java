package com.project.swimcb.swimming_pool_review.application;

import com.project.swimcb.swimming_pool_review.application.in.FindTotalReviewCountPort;
import com.project.swimcb.swimming_pool_review.domain.SwimmingPoolReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class FindTotalReviewCountInteractor implements FindTotalReviewCountPort {

  private final SwimmingPoolReviewRepository swimmingPoolReviewRepository;

  @Override
  public int findTotalReviewCount(long swimmingPoolId) {
    return swimmingPoolReviewRepository.countBySwimmingPool_Id(swimmingPoolId);
  }
}
