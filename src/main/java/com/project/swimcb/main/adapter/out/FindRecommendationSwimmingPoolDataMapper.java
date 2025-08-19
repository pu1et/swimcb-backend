package com.project.swimcb.main.adapter.out;

import com.project.swimcb.main.application.port.in.FindRecommendationSwimmingPoolDsGateway;
import com.project.swimcb.main.domain.FindRecommendationSwimmingPoolCondition;
import com.project.swimcb.main.domain.RecommendationSwimmingPool;
import java.util.List;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
class FindRecommendationSwimmingPoolDataMapper implements FindRecommendationSwimmingPoolDsGateway {

  @Override
  public List<RecommendationSwimmingPool> findRecommendationSwimmingPools(
      @NonNull FindRecommendationSwimmingPoolCondition condition) {
    return List.of();
  }

}
