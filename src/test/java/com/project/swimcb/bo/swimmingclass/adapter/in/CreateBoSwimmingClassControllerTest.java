package com.project.swimcb.bo.swimmingclass.adapter.in;

import static com.project.swimcb.bo.swimmingclass.adapter.in.CreateBoSwimmingClassControllerTest.SWIMMING_POOL_ID;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.swimmingclass.adapter.in.CreateBoSwimmingClassRequest.RegistrationCapacity;
import com.project.swimcb.bo.swimmingclass.adapter.in.CreateBoSwimmingClassRequest.Ticket;
import com.project.swimcb.bo.swimmingclass.adapter.in.CreateBoSwimmingClassRequest.Time;
import com.project.swimcb.bo.swimmingclass.adapter.in.CreateBoSwimmingClassRequest.Type;
import com.project.swimcb.bo.swimmingclass.application.in.CreateBoSwimmingClassUseCase;
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

@WebMvcTestWithoutSecurity(controllers = CreateBoSwimmingClassController.class)
@WithMockTokenInfo(swimmingPoolId = SWIMMING_POOL_ID)
class CreateBoSwimmingClassControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CreateBoSwimmingClassUseCase useCase;

  @Autowired
  private ObjectMapper objectMapper;

  static final long SWIMMING_POOL_ID = 1L;

  private static final String PATH = "/api/bo/swimming-classes";

  @Test
  @DisplayName("클래스 데이터 관리 - 클래스 추가 성공")
  void shouldCreateSuccessfully() throws Exception {
    // given
    val request = CreateBoSwimmingClassRequestFactory.create();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    verify(useCase, only()).createBoSwimmingClass(request.toCommand(SWIMMING_POOL_ID));
  }

  @Test
  @DisplayName("강습 월이 1 미만인 경우 400 반환")
  void shouldReturn400WhenMonthIsLessThan1() throws Exception {
    // given
    val request = CreateBoSwimmingClassRequest.builder()
        .month(0)
        .days(List.of())
        .time(CreateBoSwimmingClassRequestFactory.time())
        .type(CreateBoSwimmingClassRequestFactory.type())
        .instructorId(1L)
        .tickets(CreateBoSwimmingClassRequestFactory.tickets())
        .registrationCapacity(CreateBoSwimmingClassRequestFactory.registrationCapacity())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습 월은 1 이상이어야 합니다.")));
  }

  @Test
  @DisplayName("강습 월이 12 초과인 경우 400 반환")
  void shouldReturn400WhenMonthIsGreaterThan12() throws Exception {
    // given
    val request = CreateBoSwimmingClassRequest.builder()
        .month(13)
        .days(List.of())
        .time(CreateBoSwimmingClassRequestFactory.time())
        .type(CreateBoSwimmingClassRequestFactory.type())
        .instructorId(1L)
        .tickets(CreateBoSwimmingClassRequestFactory.tickets())
        .registrationCapacity(CreateBoSwimmingClassRequestFactory.registrationCapacity())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습 월은 12 이하여야 합니다.")));
  }

  @Test
  @DisplayName("강습 요일이 null인 경우 400 반환")
  void shouldReturn400WhenDayIsNull() throws Exception {
    // given
    val request = CreateBoSwimmingClassRequest.builder()
        .month(1)
        .time(CreateBoSwimmingClassRequestFactory.time())
        .type(CreateBoSwimmingClassRequestFactory.type())
        .instructorId(1L)
        .tickets(CreateBoSwimmingClassRequestFactory.tickets())
        .registrationCapacity(CreateBoSwimmingClassRequestFactory.registrationCapacity())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습 요일은 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("강습 시간이 null인 경우 400 반환")
  void shouldReturn400WhenTimeIsNull() throws Exception {
    // given
    val request = CreateBoSwimmingClassRequest.builder()
        .month(1)
        .days(List.of())
        .type(CreateBoSwimmingClassRequestFactory.type())
        .instructorId(1L)
        .tickets(CreateBoSwimmingClassRequestFactory.tickets())
        .registrationCapacity(CreateBoSwimmingClassRequestFactory.registrationCapacity())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습 시작/종료 시간은 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("강습 시작 시간이 null인 경우 400 반환")
  void shouldReturn400WhenStartTimeIsNull() throws Exception {
    // given
    val request = CreateBoSwimmingClassRequest.builder()
        .month(1)
        .days(List.of())
        .time(Time.builder().endTime(LocalTime.of(6, 50)).build())
        .type(CreateBoSwimmingClassRequestFactory.type())
        .instructorId(1L)
        .tickets(CreateBoSwimmingClassRequestFactory.tickets())
        .registrationCapacity(CreateBoSwimmingClassRequestFactory.registrationCapacity())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습 시작 시간은 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("강습 종료 시간이 null인 경우 400 반환")
  void shouldReturn400WhenEndTimeIsNull() throws Exception {
    // given
    val request = CreateBoSwimmingClassRequest.builder()
        .month(1)
        .days(List.of())
        .time(Time.builder().startTime(LocalTime.of(6, 0)).build())
        .type(CreateBoSwimmingClassRequestFactory.type())
        .instructorId(1L)
        .tickets(CreateBoSwimmingClassRequestFactory.tickets())
        .registrationCapacity(CreateBoSwimmingClassRequestFactory.registrationCapacity())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습 종료 시간은 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("강습형태ID/강습구분ID가 null인 경우 400 반환")
  void shouldReturn400WhenClassTypeIsNull() throws Exception {
    // given
    val request = CreateBoSwimmingClassRequest.builder()
        .month(1)
        .days(List.of())
        .time(CreateBoSwimmingClassRequestFactory.time())
        .instructorId(1L)
        .tickets(CreateBoSwimmingClassRequestFactory.tickets())
        .registrationCapacity(CreateBoSwimmingClassRequestFactory.registrationCapacity())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습형태ID/강습구분ID는 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("강습형태ID가 0 미만인 400 반환")
  void shouldReturn400WhenClassTypeIdIsLessThan0() throws Exception {
    // given
    val request = CreateBoSwimmingClassRequest.builder()
        .month(1)
        .days(List.of())
        .time(CreateBoSwimmingClassRequestFactory.time())
        .type(Type.builder().classTypeId(-1L).build())
        .instructorId(1L)
        .tickets(CreateBoSwimmingClassRequestFactory.tickets())
        .registrationCapacity(CreateBoSwimmingClassRequestFactory.registrationCapacity())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습형태ID는 0 이상이어야 합니다.")));
  }

  @Test
  @DisplayName("강습구분ID가 0 미만인 경우 400 반환")
  void shouldReturn400WhenClassSubTypeIdIsLessThan0() throws Exception {
    // given
    val request = CreateBoSwimmingClassRequest.builder()
        .month(1)
        .days(List.of())
        .time(CreateBoSwimmingClassRequestFactory.time())
        .type(Type.builder().classSubTypeId(-1L).build())
        .instructorId(1L)
        .tickets(CreateBoSwimmingClassRequestFactory.tickets())
        .registrationCapacity(CreateBoSwimmingClassRequestFactory.registrationCapacity())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습구분ID는 0 이상이어야 합니다.")));
  }

  @Test
  @DisplayName("담당강사 ID가 0 미만인 경우 400 반환")
  void shouldReturn400WhenInstructorIdIsLessThan0() throws Exception {
    // given
    val request = CreateBoSwimmingClassRequest.builder()
        .month(1)
        .days(List.of())
        .time(CreateBoSwimmingClassRequestFactory.time())
        .type(CreateBoSwimmingClassRequestFactory.type())
        .instructorId(-1L)
        .tickets(CreateBoSwimmingClassRequestFactory.tickets())
        .registrationCapacity(CreateBoSwimmingClassRequestFactory.registrationCapacity())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("담당강사ID는 0 이상이어야 합니다.")));
  }

  @Test
  @DisplayName("강습 티켓 리스트가 null인 경우 400 반환")
  void shouldReturn400WhenTicketsIsNull() throws Exception {
    // given
    val request = CreateBoSwimmingClassRequest.builder()
        .month(1)
        .days(List.of())
        .time(CreateBoSwimmingClassRequestFactory.time())
        .type(CreateBoSwimmingClassRequestFactory.type())
        .instructorId(1L)
        .registrationCapacity(CreateBoSwimmingClassRequestFactory.registrationCapacity())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습 티켓 리스트는 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("강습 티켓 이름이 null인 경우 400 반환")
  void shouldReturn400WhenTicketNameIsNull() throws Exception {
    // given
    val request = CreateBoSwimmingClassRequest.builder()
        .month(1)
        .days(List.of())
        .time(CreateBoSwimmingClassRequestFactory.time())
        .type(CreateBoSwimmingClassRequestFactory.type())
        .instructorId(1L)
        .tickets(List.of(Ticket.builder().price(100000).build()))
        .registrationCapacity(CreateBoSwimmingClassRequestFactory.registrationCapacity())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("티켓 이름은 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("정원 정보가 null인 경우 400 반환")
  void shouldReturn400WhenRegistrationCapacityIsNull() throws Exception {
    // given
    val request = CreateBoSwimmingClassRequest.builder()
        .month(1)
        .days(List.of())
        .time(CreateBoSwimmingClassRequestFactory.time())
        .type(CreateBoSwimmingClassRequestFactory.type())
        .instructorId(1L)
        .tickets(CreateBoSwimmingClassRequestFactory.tickets())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("정원 정보는 null이 될 수 없습니다.")));
  }

  private static class CreateBoSwimmingClassRequestFactory {

    private static CreateBoSwimmingClassRequest create() {
      return CreateBoSwimmingClassRequest.builder()
          .month(1)
          .days(List.of())
          .time(time())
          .type(type())
          .instructorId(1L)
          .tickets(tickets())
          .registrationCapacity(registrationCapacity())
          .build();
    }

    private static Time time() {
      return Time.builder()
          .startTime(LocalTime.of(6, 0))
          .endTime(LocalTime.of(6, 50))
          .build();
    }

    private static Type type() {
      return Type.builder()
          .classTypeId(1L)
          .classSubTypeId(1L)
          .build();
    }

    private static List<Ticket> tickets() {
      return List.of(
          Ticket.builder()
              .name("성인일반")
              .price(100000)
              .build()
      );
    }

    private static RegistrationCapacity registrationCapacity() {
      return RegistrationCapacity.builder()
          .totalCapacity(20)
          .reservationLimitCount(11)
          .build();
    }
  }
}