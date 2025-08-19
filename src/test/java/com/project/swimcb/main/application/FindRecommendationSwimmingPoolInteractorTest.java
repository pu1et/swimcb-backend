package com.project.swimcb.main.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.project.swimcb.main.application.port.in.FindRecommendationSwimmingPoolDsGateway;
import com.project.swimcb.main.domain.FindRecommendationSwimmingPoolCondition;
import com.project.swimcb.main.domain.RecommendationSwimmingPool;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindRecommendationSwimmingPoolInteractorTest {

  @InjectMocks
  private FindRecommendationSwimmingPoolInteractor interactor;

  @Mock
  private FindRecommendationSwimmingPoolDsGateway gateway;

  @Nested
  @DisplayName("추천 수영장 조회")
  class FindRecommendationSwimmingPools {

    @Test
    @DisplayName("성공 - 유효한 조건으로 추천 수영장 목록을 조회한다")
    void success() {
      // given
      val condition = TestFindRecommendationSwimmingPoolConditionFactory.create();
      val expectedPools = List.of(
          TestRecommendationSwimmingPoolFactory.create(),
          TestRecommendationSwimmingPoolFactory.createWithDifferentId(2L)
      );

      given(gateway.findRecommendationSwimmingPools(condition))
          .willReturn(expectedPools);

      // when
      val result = interactor.findRecommendationSwimmingPools(condition);

      // then
      assertThat(result).isEqualTo(expectedPools);
      assertThat(result).hasSize(2);

      then(gateway).should().findRecommendationSwimmingPools(condition);
    }

    @Test
    @DisplayName("성공 - 조건에 맞는 추천 수영장이 없는 경우 빈 목록을 반환한다")
    void successWithEmptyResult() {
      // given
      val condition = TestFindRecommendationSwimmingPoolConditionFactory.create();
      final List<RecommendationSwimmingPool> emptyPools = List.of();

      given(gateway.findRecommendationSwimmingPools(any())).willReturn(emptyPools);

      // when
      val result = interactor.findRecommendationSwimmingPools(condition);

      // then
      assertThat(result).isEmpty();

      then(gateway).should().findRecommendationSwimmingPools(condition);
    }

    @Test
    @DisplayName("실패 - null 조건이 전달된 경우 NullPointerException이 발생한다")
    void failWhenConditionIsNull() {
      // given
      val nullCondition = (FindRecommendationSwimmingPoolCondition) null;

      // when & then
      assertThatThrownBy(() -> interactor.findRecommendationSwimmingPools(nullCondition))
          .isInstanceOf(NullPointerException.class);
    }

  }

  private static class TestFindRecommendationSwimmingPoolConditionFactory {

    private static FindRecommendationSwimmingPoolCondition create() {
      return FindRecommendationSwimmingPoolCondition.builder()
          .memberId(1L)
          .memberLatitude(37.5665)
          .memberLongitude(126.9780)
          .build();
    }

  }

  private static class TestRecommendationSwimmingPoolFactory {

    private static RecommendationSwimmingPool create() {
      return new RecommendationSwimmingPool(
          1L,
          "https://example.com/image1.jpg",
          100L,
          500,
          "강남 수영장",
          "서울시 강남구 테스트로 123",
          4.5,
          150
      );
    }

    private static RecommendationSwimmingPool createWithDifferentId(Long swimmingPoolId) {
      return new RecommendationSwimmingPool(
          swimmingPoolId,
          "https://example.com/image2.jpg",
          200L,
          800,
          "서초 수영장",
          "서울시 서초구 테스트로 456",
          4.2,
          89
      );
    }

  }

}
