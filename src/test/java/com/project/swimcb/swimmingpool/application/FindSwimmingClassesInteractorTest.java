package com.project.swimcb.swimmingpool.application;

import static com.project.swimcb.swimmingpool.domain.enums.GroupFixedClassSubTypeName.BASIC;
import static com.project.swimcb.swimmingpool.domain.enums.GroupFixedClassSubTypeName.BEGINNER;
import static com.project.swimcb.swimmingpool.domain.enums.Sort.DISTANCE_ASC;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.GROUP;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.KIDS_SWIMMING;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingClassesResponse;
import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingClassesResponse.SwimmingClass;
import com.project.swimcb.swimmingpool.application.out.FindSwimmingClassesDsGateway;
import com.project.swimcb.swimmingpool.domain.FindSwimmingClassesCondition;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class FindSwimmingClassesInteractorTest {

  @InjectMocks
  private FindSwimmingClassesInteractor interactor;

  @Mock
  private FindSwimmingClassesDsGateway gateway;

  @Test
  @DisplayName("클래스 리스트를 성공적으로 조회한다.")
  void shouldFindSwimmingClassesSuccessfully() {
    // given
    val condition = TestFindSwimmingClassesConditionFactory.create();
    val dbResponse = TestFindSwimmingClassesResponseFactory.create();

    when(gateway.findSwimmingClasses(any())).thenReturn(dbResponse);
    // when
    val response = interactor.findSwimmingClasses(condition);
    // then
    assertThat(response).isEqualTo(dbResponse);

    verify(gateway, only()).findSwimmingClasses(condition);
  }

  private static class TestFindSwimmingClassesResponseFactory {

    private static FindSwimmingClassesResponse create() {
      val classes = List.of(SwimmingClass.builder()
          .swimmingPoolId(1L)
          .imageUrl("MOCK_IMAGE_URL")
          .favoriteId(1L)
          .distance(100)
          .name("MOCK_NAME")
          .address("MOCK_ADDRESS")
          .rating(4.5)
          .reviewCount(100)
          .minTicketPrice(10000)
          .build());
      return new FindSwimmingClassesResponse(new PageImpl<>(classes));
    }
  }

  private static class TestFindSwimmingClassesConditionFactory {

    private static FindSwimmingClassesCondition create() {
      return FindSwimmingClassesCondition.builder()
          .memberId(1L)
          .startDate(LocalDate.of(2025, 3, 1))
          .endDate(LocalDate.of(2025, 4, 1))
          .startTimes(List.of(LocalTime.of(6, 0), LocalTime.of(17, 0)))
          .days(List.of(MONDAY, TUESDAY, WEDNESDAY))
          .classTypes(List.of(GROUP, KIDS_SWIMMING))
          .classSubTypes(List.of(BASIC, BEGINNER))
          .memberLatitude(12.345)
          .memberLongitude(23.456)
          .pageable(PageRequest.of(0, 10))
          .sort(DISTANCE_ASC)
          .build();
    }
  }
}
