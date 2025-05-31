package com.project.swimcb.bo.swimmingpool.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.swimmingpool.application.out.ImageUrlPort;
import com.project.swimcb.bo.swimmingpool.domain.AccountNo;
import com.project.swimcb.bo.swimmingpool.domain.SwimmingPool;
import com.project.swimcb.bo.swimmingpool.domain.SwimmingPoolImage;
import com.project.swimcb.bo.swimmingpool.domain.SwimmingPoolImageRepository;
import com.project.swimcb.bo.swimmingpool.domain.SwimmingPoolRepository;
import java.util.List;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindSwimmingPoolBasicInfoInteractorTest {

  @InjectMocks
  private FindSwimmingPoolBasicInfoInteractor interactor;

  @Mock
  private SwimmingPoolRepository swimmingPoolRepository;

  @Mock
  private SwimmingPoolImageRepository swimmingPoolImageRepository;

  @Mock
  private ImageUrlPort imageUrlPort;

  @Nested
  @DisplayName("수영장 기본 정보 조회 테스트")
  class FindSwimmingPoolBasicInfo {

    @Nested
    @DisplayName("성공 케이스")
    class SuccessCase {

      @Test
      @DisplayName("수영장 기본 정보를 정상적으로 조회한다")
      void shouldReturnSwimmingPoolBasicInfo() {
        // given
        long swimmingPoolId = 1L;

        val pool = SwimmingPoolFactory.create();
        val images = SwimmingPoolImageFactory.create();
        val usageAgreementUrl = "DUMMY_URL1";
        val swimmingPoolImageUrl1 = "DUMMY_URL2";
        val swimmingPoolImageUrl2 = "DUMMY_URL2";

        when(swimmingPoolRepository.findById(swimmingPoolId)).thenReturn(Optional.of(pool));
        when(swimmingPoolImageRepository.findBySwimmingPoolId(swimmingPoolId)).thenReturn(images);
        when(imageUrlPort.getImageUrl(pool.getUsageAgreementPath())).thenReturn(usageAgreementUrl);
        when(imageUrlPort.getImageUrl(images.get(0).getPath())).thenReturn(swimmingPoolImageUrl1);
        when(imageUrlPort.getImageUrl(images.get(1).getPath())).thenReturn(swimmingPoolImageUrl2);
        // when
        val response = interactor.findSwimmingPoolBasicInfo(swimmingPoolId);
        // then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo(pool.getName());
        assertThat(response.newRegistrationPeriodStartDay()).isEqualTo(
            pool.getNewRegistrationPeriodStartDay());
        assertThat(response.usageAgreementUrl()).isEqualTo(usageAgreementUrl);

        assertThat(response.representativeImageUrls()).hasSize(2);
        assertThat(response.representativeImageUrls()).containsExactly(swimmingPoolImageUrl1,
            swimmingPoolImageUrl2);

        verify(swimmingPoolRepository, only()).findById(swimmingPoolId);
        verify(swimmingPoolImageRepository, only()).findBySwimmingPoolId(swimmingPoolId);
        verify(imageUrlPort, times(1)).getImageUrl(pool.getUsageAgreementPath());
        verify(imageUrlPort, times(1)).getImageUrl(images.get(0).getPath());
        verify(imageUrlPort, times(1)).getImageUrl(images.get(1).getPath());
      }
    }

    @Nested
    @DisplayName("실패 케이스")
    class FailCase {

      @Test
      @DisplayName("존재하지 않는 수영장 ID로 조회하면 IllegalArgumentException 예외가 발생한다")
      void shouldThrowIllegalArgumentExceptionWhenSwimmingPoolDoesNotFound() {
        // given
        long swimmingPoolId = 1L;

        when(swimmingPoolRepository.findById(swimmingPoolId)).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> interactor.findSwimmingPoolBasicInfo(swimmingPoolId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("수영장을 찾을 수 없습니다. " + swimmingPoolId);

        verify(swimmingPoolRepository, only()).findById(swimmingPoolId);
        verify(swimmingPoolImageRepository, never()).findBySwimmingPoolId(anyLong());
      }
    }
  }

  private static class SwimmingPoolFactory {

    private static SwimmingPool create() {
      val swimmingPool = mock(SwimmingPool.class);
      when(swimmingPool.getName()).thenReturn("DUMMY_NAME");
      when(swimmingPool.getPhone()).thenReturn("DUMMY_PHONE");
      when(swimmingPool.getAddress()).thenReturn("DUMMY_ADDRESS");
      when(swimmingPool.getNewRegistrationPeriodStartDay()).thenReturn(1);
      when(swimmingPool.getNewRegistrationPeriodEndDay()).thenReturn(2);
      when(swimmingPool.getReRegistrationPeriodStartDay()).thenReturn(3);
      when(swimmingPool.getReRegistrationPeriodEndDay()).thenReturn(4);
      when(swimmingPool.getUsageAgreementPath()).thenReturn("DUMMY_USAGE_AGREEMENT_PATH");
      when(swimmingPool.getAccountNo()).thenReturn(mock(AccountNo.class));
      return swimmingPool;
    }
  }

  private static class SwimmingPoolImageFactory {

    private static List<SwimmingPoolImage> create() {
      val swimmingPoolImage1 = mock(SwimmingPoolImage.class);
      val swimmingPoolImage2 = mock(SwimmingPoolImage.class);
      when(swimmingPoolImage1.getPath()).thenReturn("DUMMY_PATH1");
      when(swimmingPoolImage2.getPath()).thenReturn("DUMMY_PATH2");
      return List.of(swimmingPoolImage1, swimmingPoolImage2);
    }
  }
}
