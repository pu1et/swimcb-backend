package com.project.swimcb.main.application;

import com.project.swimcb.main.application.port.in.FindRecommendationSwimmingPoolDsGateway;
import com.project.swimcb.main.application.port.in.FindRecommendationSwimmingPoolUseCase;
import com.project.swimcb.main.domain.FindRecommendationSwimmingPoolCondition;
import com.project.swimcb.main.domain.RecommendationSwimmingPool;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class FindRecommendationSwimmingPoolInteractor implements
    FindRecommendationSwimmingPoolUseCase {

  private final FindRecommendationSwimmingPoolDsGateway gateway;

  @Override
  public List<RecommendationSwimmingPool> findRecommendationSwimmingPools(
      @NonNull FindRecommendationSwimmingPoolCondition condition) {

    return gateway.findRecommendationSwimmingPools(condition);
  }

}
