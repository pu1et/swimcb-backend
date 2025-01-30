package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.RecommendationSwimmingPool;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "추천")
@RestController
@RequestMapping("/api/swimming-pools/recommendation")
public class FindRecommendationSwimmingPoolController {

  @Operation(summary = "추천 수영장 조회")
  @GetMapping
  public List<RecommendationSwimmingPool> findRecommendationSwimmingPool() {
    return List.of(
        RecommendationSwimmingPool.builder()
            .imageUrl("https://ibb.co/bjGKF8WV")
            .isFavorite(false)
            .distance("400m")
            .name("올림픽 수영장")
            .address("서울시 송파구 올림픽로")
            .star("4.5")
            .reviewCount(35)
            .build(),
        RecommendationSwimmingPool.builder()
            .imageUrl("https://ibb.co/67BwJXjC")
            .isFavorite(false)
            .distance("820m")
            .name("킨디스윔")
            .address("서울시 송파구 양재대로")
            .star("4.0")
            .reviewCount(28)
            .build(),
        RecommendationSwimmingPool.builder()
            .imageUrl("https://ibb.co/zWdrk20p")
            .isFavorite(true)
            .distance("970m")
            .name("웨일즈수영장")
            .address("서울시 송파구 올림픽로")
            .star("4.1")
            .reviewCount(9)
            .build()
    );
  }
}
