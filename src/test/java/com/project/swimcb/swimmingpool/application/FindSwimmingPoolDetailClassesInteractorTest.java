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

import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailClassesCondition;
import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailClassesResponse;
import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailClassesResponse.SwimmingClass;
import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailClassesResponse.SwimmingClassTicket;
import com.project.swimcb.swimmingpool.application.out.FindSwimmingPoolDetailClassesGateway;
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
class FindSwimmingPoolDetailClassesInteractorTest {

  @InjectMocks
  private FindSwimmingPoolDetailClassesInteractor interactor;

  @Mock
  private FindSwimmingPoolDetailClassesGateway gateway;

  @Test
  @DisplayName("수영 상세 - 클래스 리스트를 성공적으로 조회한다.")
  void shouldFindSwimmingClassesSuccessfully() {
    // given
    val condition = TestFindSwimmingPoolDetailClassesConditionFactory.create();
    val dbResponse = TestFindSwimmingClassesResponseFactory.create();

    when(gateway.findSwimmingPoolDetailClasses(any())).thenReturn(dbResponse);
    // when
    val response = interactor.findSwimmingPoolDetailClasses(condition);
    // then
    assertThat(response).isEqualTo(dbResponse);

    verify(gateway, only()).findSwimmingPoolDetailClasses(condition);
  }

  private static class TestFindSwimmingClassesResponseFactory {

    private static FindSwimmingPoolDetailClassesResponse create() {
      val classes = List.of(SwimmingClass.builder()
          .swimmingClassId(1L)
          .type(GROUP.getDescription())
          .subType(BEGINNER.getDescription())
          .days(List.of("월", "화", "수"))
          .startTime(LocalTime.of(6, 0))
          .endTime(LocalTime.of(7, 0))
          .minimumPrice(10000)
          .favoriteId(1L)
          .isReservable(true)
          .tickets(List.of(
              SwimmingClassTicket.builder()
                  .swimmingClassTicketId(1L)
                  .name("DUMMY_TICKET_NAME1")
                  .price(10000)
                  .build(),
              SwimmingClassTicket.builder()
                  .swimmingClassTicketId(2L)
                  .name("DUMMY_TICKET_NAME2")
                  .price(20000)
                  .build()))
          .build());
      return new FindSwimmingPoolDetailClassesResponse(new PageImpl<>(classes));
    }
  }

  private static class TestFindSwimmingPoolDetailClassesConditionFactory {

    private static FindSwimmingPoolDetailClassesCondition create() {
      return FindSwimmingPoolDetailClassesCondition.builder()
          .swimmingPoolId(1L)
          .startDate(LocalDate.of(2025, 3, 1))
          .endDate(LocalDate.of(2025, 4, 1))
          .startTimes(List.of(LocalTime.of(6, 0), LocalTime.of(17, 0)))
          .days(List.of(MONDAY, TUESDAY, WEDNESDAY))
          .classTypes(List.of(GROUP, KIDS_SWIMMING))
          .classSubTypes(List.of(BASIC, BEGINNER))
          .pageable(PageRequest.of(0, 10))
          .memberId(2L)
          .build();
    }
  }
}
