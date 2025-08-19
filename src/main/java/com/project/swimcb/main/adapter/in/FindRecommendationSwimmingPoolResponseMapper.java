package com.project.swimcb.main.adapter.in;

import com.project.swimcb.bo.swimmingpool.application.out.ImageUrlPort;
import com.project.swimcb.main.domain.RecommendationSwimmingPool;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindRecommendationSwimmingPoolResponseMapper {

  private final ImageUrlPort imageUrlPort;

  public FindRecommendationSwimmingPoolResponse toResponse(
      @NonNull List<RecommendationSwimmingPool> result) {
    return FindRecommendationSwimmingPoolResponse.builder()
        .swimmingPools(
            result.stream()
                .map(i -> FindRecommendationSwimmingPoolResponse.SwimmingPool.builder()
                    .swimmingPoolId(i.swimmingPoolId())
                    .imageUrl(imageUrlPort.getImageUrl(i.imagePath()))
                    .favoriteId(i.favoriteId())
                    .distance(i.distance())
                    .name(i.name())
                    .address(i.address())
                    .rating(i.rating())
                    .reviewCount(i.reviewCount())
                    .build())
                .toList())
        .build();
  }

}
