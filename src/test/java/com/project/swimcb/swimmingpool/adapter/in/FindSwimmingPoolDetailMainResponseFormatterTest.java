package com.project.swimcb.swimmingpool.adapter.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.notice.application.out.ImageUrlPrefixProvider;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailMain;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindSwimmingPoolDetailMainResponseFormatterTest {

  @InjectMocks
  private FindSwimmingPoolDetailMainResponseFormatter formatter;

  @Mock
  private ImageUrlPrefixProvider imageUrlPrefixProvider;

  private final String IMAGE_URL_PREFIX = "DUMMY_IMAGE_URL_PREFIX";

  @BeforeEach
  void setUp() {
    lenient().when(imageUrlPrefixProvider.provide()).thenReturn(IMAGE_URL_PREFIX);
  }

  @Nested
  @DisplayName("수영장 상세 정보를 응답으로 변환할 때")
  class CreateFindSwimmingPoolDetailMainResponseTest {

    @Test
    @DisplayName("정상적으로 응답을 반환한다.")
    void createFindSwimmingPoolDetailMainResponse() {
      // given
      val pool = TestSwimmingPoolDetailMainFactory.create(
          List.of("DUMMY_IMAGE_PATH1", "DUMMY_IMAGE_PATH2"));
      // when
      val response = formatter.create(pool);
      // then
      assertThat(response.imageUrls())
          .containsExactly(
              IMAGE_URL_PREFIX + "DUMMY_IMAGE_PATH1",
              IMAGE_URL_PREFIX + "DUMMY_IMAGE_PATH2"
          );
      assertThat(response.name()).isEqualTo(pool.name());
      assertThat(response.isFavorite()).isEqualTo(pool.isFavorite());
      assertThat(response.rating()).isEqualTo(pool.rating());
      assertThat(response.reviewCount()).isEqualTo(pool.reviewCount());
    }

    @Test
    @DisplayName("이미지 경로가 없는 경우 빈 이미지 URL 리스트를 반환한다.")
    void createFindSwimmingPoolDetailMainResponseWithEmptyImageUrl() {
      // given
      val pool = TestSwimmingPoolDetailMainFactory.create(List.of());
      // when
      val response = formatter.create(pool);
      // then
      assertThat(response.imageUrls()).isEmpty();
      assertThat(response.name()).isEqualTo(pool.name());
      assertThat(response.isFavorite()).isEqualTo(pool.isFavorite());
      assertThat(response.rating()).isEqualTo(pool.rating());
      assertThat(response.reviewCount()).isEqualTo(pool.reviewCount());
    }
  }

  private static class TestSwimmingPoolDetailMainFactory {

    private static SwimmingPoolDetailMain create(List<String> imagePaths) {
      return SwimmingPoolDetailMain.builder()
          .imagePaths(imagePaths)
          .name("DUMMY_NAME")
          .isFavorite(true)
          .rating(4.5)
          .reviewCount(10)
          .address("DUMMY_ADDRESS")
          .phone("DUMMY_PHONE")
          .build();
    }
  }
}