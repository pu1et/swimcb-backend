package com.project.swimcb.bo.swimmingclass.application;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Instructor;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.RegistrationCapacity;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.SwimmingClass;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.TicketPriceRange;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Time;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Type;
import com.project.swimcb.bo.swimmingclass.application.out.FindBoSwimmingClassesDsGateway;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindBoSwimmingClassesInteractorTest {

  @InjectMocks
  private FindBoSwimmingClassesInteractor interactor;

  @Mock
  private FindBoSwimmingClassesDsGateway gateway;

  @Test
  @DisplayName("클래스 리스트를 성공적으로 조회한다.")
  void shouldFindSwimmingClassesSuccessfully() {
    // given
    val swimmingPoolId = 1L;
    val month = 3;
    val swimmingClasses = TestBoSwimmingClassesFactory.create();

    when(gateway.findBySwimmingPoolId(anyLong(), anyInt())).thenReturn(swimmingClasses);
    // when
    val response = interactor.findBoSwimmingClasses(swimmingPoolId, month);
    // then
    assertThat(response).isEqualTo(swimmingClasses);

    verify(gateway, only()).findBySwimmingPoolId(swimmingPoolId, month);
  }

  private static class TestBoSwimmingClassesFactory {

    private static FindBoSwimmingClassesResponse create() {
      val swimmingClasses = List.of(
          SwimmingClass.builder()
              .swimmingClassId(1L)
              .type(Type.builder()
                  .typeId(2L)
                  .typeName("DUMMY_TYPE_NAME")
                  .subTypeId(3L)
                  .subTypeName("DUMMY_SUBTYPE_NAME")
                  .build())
              .days(List.of(MONDAY, WEDNESDAY, FRIDAY))
              .time(Time.builder()
                  .startTime(LocalTime.of(9, 0))
                  .endTime(LocalTime.of(10, 0))
                  .build())
              .instructor(Instructor.builder()
                  .id(4L)
                  .name("DUMMY_INSTRUCTOR_NAME")
                  .build())
              .ticketPriceRange(TicketPriceRange.builder()
                  .minimumPrice(10000)
                  .maximumPrice(20000)
                  .build())
              .tickets(List.of(
                  FindBoSwimmingClassesResponse.Ticket.builder()
                      .id(5L)
                      .name("DUMMY_TICKET_NAME")
                      .price(10000)
                      .build(),
                  FindBoSwimmingClassesResponse.Ticket.builder()
                      .id(6L)
                      .name("DUMMY_TICKET_NAME")
                      .price(20000)
                      .build()
              ))
              .registrationCapacity(RegistrationCapacity.builder()
                  .totalCapacity(10)
                  .reservationLimitCount(5)
                  .completedReservationCount(4)
                  .remainingReservationCount(1)
                  .build())
              .build(),
          SwimmingClass.builder()
              .swimmingClassId(2L)
              .type(Type.builder()
                  .typeId(3L)
                  .typeName("DUMMY_TYPE_NAME")
                  .subTypeId(4L)
                  .subTypeName("DUMMY_SUBTYPE_NAME")
                  .build())
              .days(List.of(TUESDAY, THURSDAY))
              .time(Time.builder()
                  .startTime(LocalTime.of(9, 0))
                  .endTime(LocalTime.of(10, 0))
                  .build())
              .instructor(Instructor.builder()
                  .id(5L)
                  .name("DUMMY_INSTRUCTOR_NAME")
                  .build())
              .ticketPriceRange(TicketPriceRange.builder()
                  .minimumPrice(10000)
                  .maximumPrice(20000)
                  .build())
              .tickets(List.of(
                  FindBoSwimmingClassesResponse.Ticket.builder()
                      .id(6L)
                      .name("DUMMY_TICKET_NAME")
                      .price(10000)
                      .build(),
                  FindBoSwimmingClassesResponse.Ticket.builder()
                      .id(7L)
                      .name("DUMMY_TICKET_NAME")
                      .price(20000)
                      .build()
              ))
              .registrationCapacity(RegistrationCapacity.builder()
                  .totalCapacity(20)
                  .reservationLimitCount(10)
                  .completedReservationCount(6)
                  .remainingReservationCount(4)
                  .build())
              .build()
      );
      return new FindBoSwimmingClassesResponse(swimmingClasses);
    }
  }
}