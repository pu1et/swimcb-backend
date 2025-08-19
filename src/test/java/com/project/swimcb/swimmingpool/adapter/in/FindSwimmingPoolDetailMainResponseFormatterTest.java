package com.project.swimcb.swimmingpool.adapter.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.project.swimcb.bo.swimmingpool.application.out.ImageUrlPort;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailMain;
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
class FindSwimmingPoolDetailMainResponseFormatterTest {

  @InjectMocks
  private FindSwimmingPoolDetailMainResponseFormatter formatter;

  @Mock
  private ImageUrlPort imageUrlPort;

  @Nested
  @DisplayName("수영장 상세 정보를 응답으로 변환할 때")
  class CreateFindSwimmingPoolDetailMainResponseTest {

    @Test
    @DisplayName("정상적으로 응답을 반환한다.")
    void createFindSwimmingPoolDetailMainResponse() {
      // given
      val pool = TestSwimmingPoolDetailMainFactory.create(
          List.of("DUMMY_IMAGE_PATH1", "DUMMY_IMAGE_PATH2"));

      val expectedImageUrl1 = "NEW_DUMMY_IMAGE_PATH1";
      val expectedImageUrl2 = "NEW_DUMMY_IMAGE_PATH2";

      given(imageUrlPort.getImageUrl("DUMMY_IMAGE_PATH1")).willReturn(expectedImageUrl1);
      given(imageUrlPort.getImageUrl("DUMMY_IMAGE_PATH2")).willReturn(expectedImageUrl2);

      // when
      val response = formatter.create(pool);

      // then
      assertThat(response.imageUrls()).containsExactly(expectedImageUrl1, expectedImageUrl2);
      assertThat(response.name()).isEqualTo(pool.name());
      assertThat(response.favoriteId()).isEqualTo(pool.favoriteId());
      assertThat(response.rating()).isEqualTo(pool.rating());
      assertThat(response.reviewCount()).isEqualTo(pool.reviewCount());
      assertThat(response.latitude()).isEqualTo(pool.latitude());
      assertThat(response.longitude()).isEqualTo(pool.longitude());
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
      assertThat(response.favoriteId()).isEqualTo(pool.favoriteId());
      assertThat(response.rating()).isEqualTo(pool.rating());
      assertThat(response.reviewCount()).isEqualTo(pool.reviewCount());
    }

  }

  private static class TestSwimmingPoolDetailMainFactory {

    private static SwimmingPoolDetailMain create(List<String> imagePaths) {
      return SwimmingPoolDetailMain.builder()
          .imagePaths(imagePaths)
          .name("DUMMY_NAME")
          .favoriteId(1L)
          .rating(4.5)
          .reviewCount(10)
          .address("DUMMY_ADDRESS")
          .phone("DUMMY_PHONE")
          .latitude(37.5665)
          .longitude(126.978)
          .build();
    }

  }

}
