package com.project.swimcb.bo.swimmingpool.adapter.out;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.project.swimcb.config.s3.S3Config;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ImageUrlTest {

  @InjectMocks
  private ImageUrl imageUrl;

  @Mock
  private S3Config s3Config;

  @Test
  @DisplayName("이미지 경로가 null일 경우 null을 반환한다.")
  void shouldReturnNullWhenImagePathIsNull() {
    // given
    final String imagePath = null;
    // when
    val result = imageUrl.getImageUrl(imagePath);
    // then
    assertThat(result).isNull();

    verifyNoInteractions(s3Config);
  }

  @Test
  @DisplayName("이미지 경로가 null이 아닐 경우 이미지 URL을 반환한다.")
  void shouldReturnImageUrlWhenImagePathIsNotNull() {
    // given
    val imagePath = "DUMMY_IMAGE_PATH";
    val host = "DUMMY_HOST";
    val bucketName = "DUMMY_BUCKET_NAME";
    val expected = host + "/" + bucketName + "/" + imagePath;

    when(s3Config.getHost()).thenReturn(host);
    when(s3Config.getBucketName()).thenReturn(bucketName);
    // when
    val result = imageUrl.getImageUrl(imagePath);
    // then
    assertThat(result).isEqualTo(expected);
  }
}