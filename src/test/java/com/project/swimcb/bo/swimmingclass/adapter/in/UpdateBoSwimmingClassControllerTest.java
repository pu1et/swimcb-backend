package com.project.swimcb.bo.swimmingclass.adapter.in;

import static com.project.swimcb.bo.swimmingclass.adapter.in.UpdateBoSwimmingClassControllerTest.SWIMMING_POOL_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.swimmingclass.adapter.in.UpdateBoSwimmingClassRequest.Days;
import com.project.swimcb.bo.swimmingclass.adapter.in.UpdateBoSwimmingClassRequest.RegistrationCapacity;
import com.project.swimcb.bo.swimmingclass.adapter.in.UpdateBoSwimmingClassRequest.SwimmingClass;
import com.project.swimcb.bo.swimmingclass.adapter.in.UpdateBoSwimmingClassRequest.Ticket;
import com.project.swimcb.bo.swimmingclass.adapter.in.UpdateBoSwimmingClassRequest.Time;
import com.project.swimcb.bo.swimmingclass.adapter.in.UpdateBoSwimmingClassRequest.Type;
import com.project.swimcb.bo.swimmingclass.application.in.UpdateBoSwimmingClassUseCase;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand;
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

@WebMvcTestWithoutSecurity(controllers = UpdateBoSwimmingClassController.class)
@WithMockTokenInfo(swimmingPoolId = SWIMMING_POOL_ID)
class UpdateBoSwimmingClassControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private UpdateBoSwimmingClassUseCase useCase;

  static final long SWIMMING_POOL_ID = 1L;

  private static final String PATH = "/api/bo/swimming-classes/2";
  private static final long SWIMMING_CLASS_ID = 2L;

  @Test
  @DisplayName("클래스 데이터 관리 - 클래스 수정 성공")
  void shouldUpdateSuccessfully() throws Exception {
    // given
    val request = UpdateBoSwimmingClassRequestFactory.create();
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    verify(useCase).updateBoSwimmingClass(assertArg(i -> {
      assertThat(i.swimmingPoolId()).isEqualTo(SWIMMING_POOL_ID);
      assertThat(i.swimmingClassId()).isEqualTo(SWIMMING_CLASS_ID);
      assertThat(i.days().isMonday()).isEqualTo(request.swimmingClass().days().isMonday());
      assertThat(i.days().isTuesday()).isEqualTo(request.swimmingClass().days().isTuesday());
      assertThat(i.time().startTime()).isEqualTo(request.swimmingClass().time().startTime());
      assertThat(i.instructorId()).isEqualTo(request.swimmingClass().instructorId());
      assertThat(i.tickets()).extracting(UpdateBoSwimmingClassCommand.Ticket::name)
          .containsExactlyElementsOf(
              request.swimmingClass().tickets().stream().map(Ticket::name).toList());
      assertThat(i.registrationCapacity().totalCapacity())
          .isEqualTo(request.swimmingClass().registrationCapacity().totalCapacity());
      assertThat(i.isExposed()).isEqualTo(request.swimmingClass().isExposed());
    }));
  }

  @Test
  @DisplayName("클래스가 null인 경우 400 반환")
  void shouldReturn400WhenClassesIsNull() throws Exception {
    // given
    val request = UpdateBoSwimmingClassRequest.builder().build();
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("클래스는 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("강습 요일이 null인 경우 400 반환")
  void shouldReturn400WhenDayIsNull() throws Exception {
    // given
    val request = UpdateBoSwimmingClassRequest.builder()
        .swimmingClass(
            SwimmingClass.builder()
                .time(UpdateBoSwimmingClassRequestFactory.time())
                .type(UpdateBoSwimmingClassRequestFactory.type())
                .instructorId(1L)
                .tickets(UpdateBoSwimmingClassRequestFactory.tickets())
                .registrationCapacity(
                    UpdateBoSwimmingClassRequestFactory.registrationCapacity())
                .isExposed(true)
                .build()).build();
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습 요일은 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("강습 시간이 null인 경우 400 반환")
  void shouldReturn400WhenTimeIsNull() throws Exception {
    // given
    val request = UpdateBoSwimmingClassRequest.builder()
        .swimmingClass(
            SwimmingClass.builder()
                .days(UpdateBoSwimmingClassRequestFactory.days())
                .type(UpdateBoSwimmingClassRequestFactory.type())
                .instructorId(1L)
                .tickets(UpdateBoSwimmingClassRequestFactory.tickets())
                .registrationCapacity(
                    UpdateBoSwimmingClassRequestFactory.registrationCapacity())
                .isExposed(true)
                .build()).build();
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습 시작/종료 시간은 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("강습 시작 시간이 null인 경우 400 반환")
  void shouldReturn400WhenStartTimeIsNull() throws Exception {
    // given
    val request = UpdateBoSwimmingClassRequest.builder()
        .swimmingClass(
            SwimmingClass.builder()
                .days(UpdateBoSwimmingClassRequestFactory.days())
                .time(Time.builder().endTime(LocalTime.of(6, 50)).build())
                .type(UpdateBoSwimmingClassRequestFactory.type())
                .instructorId(1L)
                .tickets(UpdateBoSwimmingClassRequestFactory.tickets())
                .registrationCapacity(
                    UpdateBoSwimmingClassRequestFactory.registrationCapacity())
                .isExposed(true)
                .build()).build();
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습 시작 시간은 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("강습 종료 시간이 null인 경우 400 반환")
  void shouldReturn400WhenEndTimeIsNull() throws Exception {
    // given
    val request = UpdateBoSwimmingClassRequest.builder()
        .swimmingClass(
            SwimmingClass.builder()
                .days(UpdateBoSwimmingClassRequestFactory.days())
                .time(Time.builder().startTime(LocalTime.of(6, 0)).build())
                .type(UpdateBoSwimmingClassRequestFactory.type())
                .instructorId(1L)
                .tickets(UpdateBoSwimmingClassRequestFactory.tickets())
                .registrationCapacity(
                    UpdateBoSwimmingClassRequestFactory.registrationCapacity())
                .isExposed(true)
                .build()).build();
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습 종료 시간은 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("강습 형태/구분이 null인 경우 400 반환")
  void shouldReturn400WhenClassTypeIsNull() throws Exception {
    // given
    val request = UpdateBoSwimmingClassRequest.builder()
        .swimmingClass(
            SwimmingClass.builder()
                .days(UpdateBoSwimmingClassRequestFactory.days())
                .time(UpdateBoSwimmingClassRequestFactory.time())
                .instructorId(1L)
                .tickets(UpdateBoSwimmingClassRequestFactory.tickets())
                .registrationCapacity(
                    UpdateBoSwimmingClassRequestFactory.registrationCapacity())
                .isExposed(true)
                .build()).build();
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습 형태/구분은 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("강습 티켓 리스트가 null인 경우 400 반환")
  void shouldReturn400WhenTicketsIsNull() throws Exception {
    // given
    val request = UpdateBoSwimmingClassRequest.builder()
        .swimmingClass(
            SwimmingClass.builder()
                .days(UpdateBoSwimmingClassRequestFactory.days())
                .time(UpdateBoSwimmingClassRequestFactory.time())
                .type(UpdateBoSwimmingClassRequestFactory.type())
                .instructorId(1L)
                .registrationCapacity(
                    UpdateBoSwimmingClassRequestFactory.registrationCapacity())
                .isExposed(true)
                .build()).build();
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습 티켓 리스트는 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("강습 티켓 이름이 null인 경우 400 반환")
  void shouldReturn400WhenTicketNameIsNull() throws Exception {
    // given
    val request = UpdateBoSwimmingClassRequest.builder()
        .swimmingClass(
            SwimmingClass.builder()
                .days(UpdateBoSwimmingClassRequestFactory.days())
                .time(UpdateBoSwimmingClassRequestFactory.time())
                .type(UpdateBoSwimmingClassRequestFactory.type())
                .instructorId(1L)
                .tickets(List.of(Ticket.builder().price(100000).build()))
                .registrationCapacity(
                    UpdateBoSwimmingClassRequestFactory.registrationCapacity())
                .isExposed(true)
                .build()).build();
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("티켓 이름은 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("정원 정보가 null인 경우 400 반환")
  void shouldReturn400WhenRegistrationCapacityIsNull() throws Exception {
    // given
    val request = UpdateBoSwimmingClassRequest.builder()
        .swimmingClass(
            SwimmingClass.builder()
                .days(UpdateBoSwimmingClassRequestFactory.days())
                .time(UpdateBoSwimmingClassRequestFactory.time())
                .type(UpdateBoSwimmingClassRequestFactory.type())
                .instructorId(1L)
                .tickets(UpdateBoSwimmingClassRequestFactory.tickets())
                .isExposed(true)
                .build()).build();
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("정원 정보는 null이 될 수 없습니다.")));
  }

  private static class UpdateBoSwimmingClassRequestFactory {

    private static UpdateBoSwimmingClassRequest create() {
      return UpdateBoSwimmingClassRequest.builder()
          .swimmingClass(swimmingClass())
          .build();
    }

    private static SwimmingClass swimmingClass() {
      return SwimmingClass.builder()
          .days(days())
          .time(time())
          .type(type())
          .instructorId(1L)
          .tickets(tickets())
          .registrationCapacity(registrationCapacity())
          .isExposed(true)
          .build();
    }

    private static Days days() {
      return Days.builder()
          .isMonday(true)
          .isTuesday(false)
          .isWednesday(true)
          .isThursday(false)
          .isFriday(false)
          .isSaturday(false)
          .isSunday(false)
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
          .typeId(1L)
          .subTypeId(1L)
          .build();
    }

    private static List<Ticket> tickets() {
      return List.of(
          Ticket.builder()
              .name("DUMMY_TICKET_NAME1")
              .price(10000)
              .build(),
          Ticket.builder()
              .name("DUMMY_TICKET_NAME2")
              .price(20000)
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