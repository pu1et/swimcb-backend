package com.project.swimcb.swimmingpool.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.project.swimcb.swimmingpool.application.in.FindFreeSwimmingCondition;
import com.project.swimcb.swimmingpool.application.out.FindFreeSwimmingDsGateway;
import com.project.swimcb.swimmingpool.domain.FreeSwimming;
import java.time.LocalDate;
import java.time.LocalTime;
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
class FindFreeSwimmingInteractorTest {

  @InjectMocks
  private FindFreeSwimmingInteractor interactor;

  @Mock
  private FindFreeSwimmingDsGateway gateway;

  @Nested
  @DisplayName("자유 수영장 조회 시")
  class whenFindingFreeSwimming {

    @Test
    @DisplayName("조건이 유효하면 조회한 결과를 반환한다")
    void shouldReturnFreeSwimmingListWhenConditionIsValid() {
      // given
      val condition = createValidCondition();
      val expectedResult = createFreeSwimmingList();

      given(gateway.findFreeSwimming(condition)).willReturn(expectedResult);

      // when
      val actualResult = interactor.findFreeSwimming(condition);

      // then
      assertThat(actualResult).isEqualTo(expectedResult);

      then(gateway).should().findFreeSwimming(condition);
    }

    @Test
    @DisplayName("조건이 null이면 예외를 발생시킨다")
    void shouldThrowExceptionWhenConditionIsNull() {
      // given
      val nullCondition = (FindFreeSwimmingCondition) null;

      // when
      // then
      assertThatThrownBy(() -> interactor.findFreeSwimming(nullCondition))
          .isInstanceOf(NullPointerException.class);
    }

  }

  private FindFreeSwimmingCondition createValidCondition() {
    return FindFreeSwimmingCondition.builder()
        .memberId(1L)
        .memberLatitude(37.5665)
        .memberLongitude(126.9780)
        .minLatitude(37.5565)
        .maxLatitude(37.5765)
        .minLongitude(126.9680)
        .maxLongitude(126.9880)
        .isTodayAvailable(false)
        .date(LocalDate.of(2025, 7, 15))
        .startTimes(List.of(LocalTime.of(9, 0)))
        .build();
  }

  private List<FreeSwimming> createFreeSwimmingList() {
    return List.of(
        FreeSwimming.builder()
            .swimmingPoolId(1L)
            .imagePath("/images/pool1.jpg")
            .isFavorite(false)
            .distance(200)
            .name("테스트 수영장")
            .address("서울시 강남구 테스트로 1")
            .rating(4.5)
            .reviewCount(10)
            .latitude(37.5665)
            .longitude(126.9780)
            .build()
    );
  }

}
