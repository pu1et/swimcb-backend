package com.project.swimcb.main.adapter.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.project.swimcb.bo.swimmingpool.application.out.ImageUrlPort;
import com.project.swimcb.main.domain.RecommendationSwimmingPool;
import java.util.Collections;
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
class FindRecommendationSwimmingPoolResponseMapperTest {

  @InjectMocks
  private FindRecommendationSwimmingPoolResponseMapper responseMapper;

  @Mock
  private ImageUrlPort imageUrlPort;

  @Nested
  @DisplayName("응답 매핑")
  class ToResponse {

    @Test
    @DisplayName("성공 - 추천 수영장 목록을 응답 객체로 매핑한다")
    void success() {
      // given
      val recommendationPools = List.of(
          TestRecommendationSwimmingPoolFactory.create(),
          TestRecommendationSwimmingPoolFactory.createWithDifferentId(2L)
      );

      val expectedImageUrl1 = "https://cdn.example.com/image1.jpg";
      val expectedImageUrl2 = "https://cdn.example.com/image2.jpg";

      given(imageUrlPort.getImageUrl("https://example.com/image1.jpg"))
          .willReturn(expectedImageUrl1);
      given(imageUrlPort.getImageUrl("https://example.com/image2.jpg"))
          .willReturn(expectedImageUrl2);

      // when
      val response = responseMapper.toResponse(recommendationPools);

      // then
      assertThat(response).isNotNull();
      assertThat(response.swimmingPools()).hasSize(2);

      val firstPool = response.swimmingPools().get(0);
      assertThat(firstPool.swimmingPoolId()).isEqualTo(1L);
      assertThat(firstPool.imageUrl()).isEqualTo(expectedImageUrl1);
      assertThat(firstPool.favoriteId()).isEqualTo(100L);
      assertThat(firstPool.distance()).isEqualTo(500);
      assertThat(firstPool.name()).isEqualTo("강남 수영장");
      assertThat(firstPool.address()).isEqualTo("서울시 강남구 테스트로 123");
      assertThat(firstPool.rating()).isEqualTo(4.5);
      assertThat(firstPool.reviewCount()).isEqualTo(150);

      val secondPool = response.swimmingPools().get(1);
      assertThat(secondPool.swimmingPoolId()).isEqualTo(2L);
      assertThat(secondPool.imageUrl()).isEqualTo(expectedImageUrl2);
      assertThat(secondPool.favoriteId()).isEqualTo(200L);
      assertThat(secondPool.distance()).isEqualTo(800);
      assertThat(secondPool.name()).isEqualTo("서초 수영장");
      assertThat(secondPool.address()).isEqualTo("서울시 서초구 테스트로 456");
      assertThat(secondPool.rating()).isEqualTo(4.2);
      assertThat(secondPool.reviewCount()).isEqualTo(89);

      then(imageUrlPort).should().getImageUrl("https://example.com/image1.jpg");
      then(imageUrlPort).should().getImageUrl("https://example.com/image2.jpg");
    }

    @Test
    @DisplayName("성공 - 빈 목록을 응답 객체로 매핑한다")
    void successWithEmptyList() {
      // given
      val emptyRecommendationPools = Collections.<RecommendationSwimmingPool>emptyList();

      // when
      val response = responseMapper.toResponse(emptyRecommendationPools);

      // then
      assertThat(response).isNotNull();
      assertThat(response.swimmingPools()).isEmpty();
    }

    @Test
    @DisplayName("성공 - favoriteId가 null인 수영장을 매핑한다")
    void successWithNullFavoriteId() {
      // given
      val recommendationPools = List.of(
          TestRecommendationSwimmingPoolFactory.createWithoutFavorite()
      );

      val expectedImageUrl = "https://cdn.example.com/image3.jpg";

      given(imageUrlPort.getImageUrl("https://example.com/image3.jpg"))
          .willReturn(expectedImageUrl);

      // when
      val response = responseMapper.toResponse(recommendationPools);

      // then
      assertThat(response).isNotNull();
      assertThat(response.swimmingPools()).hasSize(1);

      val pool = response.swimmingPools().get(0);
      assertThat(pool.swimmingPoolId()).isEqualTo(3L);
      assertThat(pool.imageUrl()).isEqualTo(expectedImageUrl);
      assertThat(pool.favoriteId()).isNull();
      assertThat(pool.distance()).isEqualTo(1200);
      assertThat(pool.name()).isEqualTo("송파 수영장");
      assertThat(pool.address()).isEqualTo("서울시 송파구 테스트로 789");
      assertThat(pool.rating()).isEqualTo(3.8);
      assertThat(pool.reviewCount()).isEqualTo(45);

      then(imageUrlPort).should().getImageUrl("https://example.com/image3.jpg");
    }

    @Test
    @DisplayName("실패 - null 목록이 전달된 경우 NullPointerException이 발생한다")
    void failWhenResultIsNull() {
      // given
      val nullResult = (List<RecommendationSwimmingPool>) null;

      // when
      // then
      assertThatThrownBy(() -> responseMapper.toResponse(nullResult))
          .isInstanceOf(NullPointerException.class);
    }
  }

  // Test Factory Classes
  private static class TestRecommendationSwimmingPoolFactory {

    private static RecommendationSwimmingPool create() {
      return RecommendationSwimmingPool.builder()
          .swimmingPoolId(1L)
          .imageUrl("https://example.com/image1.jpg")
          .favoriteId(100L)
          .distance(500)
          .name("강남 수영장")
          .address("서울시 강남구 테스트로 123")
          .rating(4.5)
          .reviewCount(150)
          .build();
    }

    private static RecommendationSwimmingPool createWithDifferentId(Long swimmingPoolId) {
      return RecommendationSwimmingPool.builder()
          .swimmingPoolId(swimmingPoolId)
          .imageUrl("https://example.com/image2.jpg")
          .favoriteId(200L)
          .distance(800)
          .name("서초 수영장")
          .address("서울시 서초구 테스트로 456")
          .rating(4.2)
          .reviewCount(89)
          .build();
    }

    private static RecommendationSwimmingPool createWithoutFavorite() {
      return RecommendationSwimmingPool.builder()
          .swimmingPoolId(3L)
          .imageUrl("https://example.com/image3.jpg")
          .favoriteId(null)
          .distance(1200)
          .name("송파 수영장")
          .address("서울시 송파구 테스트로 789")
          .rating(3.8)
          .reviewCount(45)
          .build();
    }
  }
}
