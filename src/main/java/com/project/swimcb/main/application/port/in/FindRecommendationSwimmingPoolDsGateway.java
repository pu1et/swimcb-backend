package com.project.swimcb.main.application.port.in;

import com.project.swimcb.main.domain.FindRecommendationSwimmingPoolCondition;
import com.project.swimcb.main.domain.RecommendationSwimmingPool;
import java.util.List;
import lombok.NonNull;

public interface FindRecommendationSwimmingPoolDsGateway {

  List<RecommendationSwimmingPool> findRecommendationSwimmingPools(
      @NonNull FindRecommendationSwimmingPoolCondition condition);

}
