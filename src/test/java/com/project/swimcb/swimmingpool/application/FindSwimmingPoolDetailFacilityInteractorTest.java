package com.project.swimcb.swimmingpool.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.project.swimcb.db.entity.SwimmingPoolEntity;
import com.project.swimcb.db.repository.SwimmingPoolRepository;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFacility;
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
class FindSwimmingPoolDetailFacilityInteractorTest {

  @InjectMocks
  private FindSwimmingPoolDetailFacilityInteractor interactor;

  @Mock
  private SwimmingPoolRepository swimmingPoolRepository;

  @Nested
  @DisplayName("수영장 시설 정보 조회")
  class FindSwimmingPoolDetailFacility {

    @Nested
    @DisplayName("유효한 수영장 ID가 주어진 경우")
    class WhenValidSwimmingPoolIdGiven {

      @Test
      @DisplayName("수영장 정보를 조회하고 SwimmingPoolDetailFacility 객체를 반환한다")
      void shouldFindSwimmingPoolAndReturnFacility() {
        // given
        val swimmingPoolId = 1L;
        val swimmingPool = mock(SwimmingPoolEntity.class);
        given(swimmingPool.getOperatingDays()).willReturn("월-금");
        given(swimmingPool.getClosedDays()).willReturn("토-일");
        given(swimmingPool.getNewRegistrationPeriodStartDay()).willReturn(1);
        given(swimmingPool.getNewRegistrationPeriodEndDay()).willReturn(7);
        given(swimmingPool.getReRegistrationPeriodStartDay()).willReturn(1);
        given(swimmingPool.getReRegistrationPeriodEndDay()).willReturn(7);
        given(swimmingPoolRepository.findById(swimmingPoolId)).willReturn(Optional.of(swimmingPool));

        // when
        val result = interactor.findSwimmingPoolDetailFacility(swimmingPoolId);

        // then
        then(swimmingPoolRepository).should().findById(swimmingPoolId);
        assertThat(result).isInstanceOf(SwimmingPoolDetailFacility.class);
        assertThat(result.operatingDays()).isEqualTo("월-금");
        assertThat(result.closedDays()).isEqualTo("토-일");
        assertThat(result.newRegistrationPeriodStartDay()).isEqualTo(1);
        assertThat(result.newRegistrationPeriodEndDay()).isEqualTo(7);
        assertThat(result.reRegistrationPeriodStartDay()).isEqualTo(1);
        assertThat(result.reRegistrationPeriodEndDay()).isEqualTo(7);
      }
    }

    @Nested
    @DisplayName("존재하지 않는 수영장 ID가 주어진 경우")
    class WhenNonExistentSwimmingPoolIdGiven {

      @Test
      @DisplayName("IllegalArgumentException을 발생시킨다")
      void shouldThrowIllegalArgumentException() {
        // given
        val swimmingPoolId = 999L;
        given(swimmingPoolRepository.findById(swimmingPoolId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> interactor.findSwimmingPoolDetailFacility(swimmingPoolId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("해당 수영장 정보가 존재하지 않습니다 : " + swimmingPoolId);

        then(swimmingPoolRepository).should().findById(swimmingPoolId);
      }
    }

    @Nested
    @DisplayName("null 수영장 ID가 주어진 경우")
    class WhenNullSwimmingPoolIdGiven {

      @Test
      @DisplayName("NullPointerException을 발생시킨다")
      void shouldThrowNullPointerException() {
        // given
        final Long swimmingPoolId = null;

        // when
        // then
        assertThatThrownBy(() -> interactor.findSwimmingPoolDetailFacility(swimmingPoolId))
            .isInstanceOf(NullPointerException.class);
      }
    }
  }
}
