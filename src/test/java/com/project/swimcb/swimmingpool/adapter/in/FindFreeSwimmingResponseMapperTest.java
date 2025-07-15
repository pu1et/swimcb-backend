package com.project.swimcb.swimmingpool.adapter.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.swimmingpool.application.out.ImageUrlPort;
import com.project.swimcb.swimmingpool.domain.FreeSwimming;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindFreeSwimmingResponseMapperTest {

  @InjectMocks
  private FindFreeSwimmingResponseMapper mapper;

  @Mock
  private ImageUrlPort imageUrlPort;

  @BeforeEach
  void setUp() {
    imageUrlPort = mock(ImageUrlPort.class);
    mapper = new FindFreeSwimmingResponseMapper(imageUrlPort);
  }

  @Test
  @DisplayName("FreeSwimming 리스트를 받아서 response로 올바르게 매핑해야 한다.")
  void shouldMapFreeSwimmingListToResponse() {
    // given
    val freeSwimmings = List.of(
        FreeSwimming.builder()
            .swimmingPoolId(1L)
            .imagePath("test-image.jpg")
            .isFavorite(true)
            .distance(10)
            .name("테스트 수영장")
            .address("테스트 주소")
            .rating(4.5)
            .reviewCount(100)
            .latitude(37.5665)
            .longitude(126.9784)
            .build()
    );

    val imageUrl = "http://test.com/test-image.jpg";
    when(imageUrlPort.getImageUrl(anyString())).thenReturn(imageUrl);

    // when
    val response = mapper.toResponse(freeSwimmings);

    // then
    assertThat(response).isNotNull();
    assertThat(response.freeSwimmings()).hasSize(1);

    val firstFreeSwimming = response.freeSwimmings().getFirst();
    assertThat(firstFreeSwimming.swimmingPoolId()).isEqualTo(1L);
    assertThat(firstFreeSwimming.imageUrl()).isEqualTo(imageUrl);
    assertThat(firstFreeSwimming.isFavorite()).isTrue();
    assertThat(firstFreeSwimming.distance()).isEqualTo(10);
    assertThat(firstFreeSwimming.name()).isEqualTo("테스트 수영장");
    assertThat(firstFreeSwimming.address()).isEqualTo("테스트 주소");
    assertThat(firstFreeSwimming.rating()).isEqualTo(4.5);
    assertThat(firstFreeSwimming.reviewCount()).isEqualTo(100);
    assertThat(firstFreeSwimming.latitude()).isEqualTo(37.5665);
  }

}
