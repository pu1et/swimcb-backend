package com.project.swimcb.bo.swimmingclass.adapter.in;

import static com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesControllerTest.SWIMMING_POOL_ID;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Days;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Instructor;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.RegistrationCapacity;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.SwimmingClass;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.TicketPriceRange;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Time;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Type;
import com.project.swimcb.bo.swimmingclass.application.in.FindBoSwimmingClassesUseCase;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindBoSwimmingClassesController.class)
@WithMockTokenInfo(swimmingPoolId = SWIMMING_POOL_ID)
class FindBoSwimmingClassesControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private FindBoSwimmingClassesUseCase useCase;

  @Autowired
  private ObjectMapper objectMapper;

  static final long SWIMMING_POOL_ID = 1L;

  private static final String PATH = "/api/bo/swimming-classes";
  private static final int MONTH = 1;

  @Test
  @DisplayName("수영 클래스 조회 성공")
  void shouldReturnResponseWhenSwimmingClassesExist() throws Exception {
    // given
    val response = TestBoSwimmingClassesFactory.create();

    when(useCase.findBoSwimmingClasses(anyLong(), anyInt())).thenReturn(response);
    // when
    // then
    mockMvc.perform(get(PATH)
            .param("month", String.valueOf(MONTH)))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));

    verify(useCase, only()).findBoSwimmingClasses(SWIMMING_POOL_ID, MONTH);
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
              .days(Days.builder()
                  .isMonday(true)
                  .isTuesday(false)
                  .isWednesday(true)
                  .isThursday(false)
                  .isFriday(true)
                  .isSaturday(false)
                  .isSunday(false)
                  .build())
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
              .days(Days.builder()
                  .isMonday(false)
                  .isTuesday(true)
                  .isWednesday(false)
                  .isThursday(true)
                  .isFriday(false)
                  .isSaturday(false)
                  .isSunday(false)
                  .build())
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