package com.project.swimcb.swimmingpool.adapter.out;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.project.swimcb.swimmingpool.domain.FindSwimmingPoolDetailFacilityResponse;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFacility;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindSwimmingPoolDetailFacilityResponseMapperTest {

  @InjectMocks
  private FindSwimmingPoolDetailFacilityResponseMapper mapper;

  @Nested
  @DisplayName("시설 정보 응답 매핑")
  class ToResponse {

    @Nested
    @DisplayName("유효한 SwimmingPoolDetailFacility가 주어진 경우")
    class WhenValidSwimmingPoolDetailFacilityGiven {

      @Test
      @DisplayName("FindSwimmingPoolDetailFacilityResponse로 올바르게 매핑된다")
      void shouldMapToFindSwimmingPoolDetailFacilityResponse() {
        // given
        val facility = SwimmingPoolDetailFacility.builder()
            .operatingDays("월-금")
            .closedDays("토-일")
            .newRegistrationPeriodStartDay(1)
            .newRegistrationPeriodEndDay(7)
            .reRegistrationPeriodStartDay(1)
            .reRegistrationPeriodEndDay(7)
            .build();

        // when
        val result = mapper.toResponse(facility);

        // then
        assertThat(result).isInstanceOf(FindSwimmingPoolDetailFacilityResponse.class);
        assertThat(result.operatingDays()).isEqualTo("월-금");
        assertThat(result.closedDays()).isEqualTo("토-일");
        assertThat(result.newRegistrationPeriodStartDay()).isEqualTo(1);
        assertThat(result.newRegistrationPeriodEndDay()).isEqualTo(7);
        assertThat(result.reRegistrationPeriodStartDay()).isEqualTo(1);
        assertThat(result.reRegistrationPeriodEndDay()).isEqualTo(7);
      }

    }

    @Nested
    @DisplayName("null SwimmingPoolDetailFacility가 주어진 경우")
    class WhenNullSwimmingPoolDetailFacilityGiven {

      @Test
      @DisplayName("NullPointerException을 발생시킨다")
      void shouldThrowNullPointerException() {
        // given
        SwimmingPoolDetailFacility facility = null;

        // when & then
        assertThatThrownBy(() -> mapper.toResponse(facility))
            .isInstanceOf(NullPointerException.class);
      }

    }

  }

}
