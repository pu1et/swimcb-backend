package com.project.swimcb.main.adapter.in;

import com.project.swimcb.main.domain.RecommendationSwimmingPool;
import java.util.List;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class FindRecommendationSwimmingPoolResponseMapper {

  public FindRecommendationSwimmingPoolResponse toResponse(
      @NonNull List<RecommendationSwimmingPool> result) {
    return null;
  }

}
