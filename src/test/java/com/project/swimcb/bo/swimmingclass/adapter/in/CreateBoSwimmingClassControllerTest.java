package com.project.swimcb.bo.swimmingclass.adapter.in;

import static com.project.swimcb.bo.swimmingclass.domain.enums.CreateBoSwimmingClassSwimmingClassType.GROUP;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.swimmingclass.adapter.in.CreateBoSwimmingClassRequest.Days;
import com.project.swimcb.bo.swimmingclass.adapter.in.CreateBoSwimmingClassRequest.RegistrationCapacity;
import com.project.swimcb.bo.swimmingclass.adapter.in.CreateBoSwimmingClassRequest.Ticket;
import com.project.swimcb.bo.swimmingclass.adapter.in.CreateBoSwimmingClassRequest.Time;
import com.project.swimcb.bo.swimmingclass.adapter.in.CreateBoSwimmingClassRequest.Type;
import com.project.swimcb.config.security.SecurityConfig;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CreateBoSwimmingClassController.class)
@Import(SecurityConfig.class)
class CreateBoSwimmingClassControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

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
  }

  @Test
  @DisplayName("강습 요일이 null인 경우 400 반환")
  void shouldReturn400WhenDayIsNull() throws Exception {
    // given
    val request = CreateBoSwimmingClassRequest.builder()
        .time(CreateBoSwimmingClassRequestFactory.time())
        .type(CreateBoSwimmingClassRequestFactory.type())
        .instructorName("손지혜")
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
        .days(CreateBoSwimmingClassRequestFactory.days())
        .type(CreateBoSwimmingClassRequestFactory.type())
        .instructorName("손지혜")
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
        .days(CreateBoSwimmingClassRequestFactory.days())
        .time(Time.builder().endTime(LocalTime.of(6, 50)).build())
        .type(CreateBoSwimmingClassRequestFactory.type())
        .instructorName("손지혜")
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
        .days(CreateBoSwimmingClassRequestFactory.days())
        .time(Time.builder().startTime(LocalTime.of(6, 0)).build())
        .type(CreateBoSwimmingClassRequestFactory.type())
        .instructorName("손지혜")
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
  @DisplayName("강습 형태/구분이 null인 경우 400 반환")
  void shouldReturn400WhenClassTypeIsNull() throws Exception {
    // given
    val request = CreateBoSwimmingClassRequest.builder()
        .days(CreateBoSwimmingClassRequestFactory.days())
        .time(CreateBoSwimmingClassRequestFactory.time())
        .instructorName("손지혜")
        .tickets(CreateBoSwimmingClassRequestFactory.tickets())
        .registrationCapacity(CreateBoSwimmingClassRequestFactory.registrationCapacity())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습 형태/구분은 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("강습 형태가 null인 경우 400 반환")
  void shouldReturn400WhenTypeIsNull() throws Exception {
    // given
    val request = CreateBoSwimmingClassRequest.builder()
        .days(CreateBoSwimmingClassRequestFactory.days())
        .time(CreateBoSwimmingClassRequestFactory.time())
        .type(Type.builder().subType("초급").build())
        .instructorName("손지혜")
        .tickets(CreateBoSwimmingClassRequestFactory.tickets())
        .registrationCapacity(CreateBoSwimmingClassRequestFactory.registrationCapacity())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습 형태는 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("강습 구분이 null인 경우 400 반환")
  void shouldReturn400WhenSubTypeIsNull() throws Exception {
    // given
    val request = CreateBoSwimmingClassRequest.builder()
        .days(CreateBoSwimmingClassRequestFactory.days())
        .time(CreateBoSwimmingClassRequestFactory.time())
        .type(Type.builder().type(GROUP).build())
        .instructorName("손지혜")
        .tickets(CreateBoSwimmingClassRequestFactory.tickets())
        .registrationCapacity(CreateBoSwimmingClassRequestFactory.registrationCapacity())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습 구분은 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("담당강사가 null인 경우 400 반환")
  void shouldReturn400WhenInstructorIsNull() throws Exception {
    // given
    val request = CreateBoSwimmingClassRequest.builder()
        .days(CreateBoSwimmingClassRequestFactory.days())
        .time(CreateBoSwimmingClassRequestFactory.time())
        .type(CreateBoSwimmingClassRequestFactory.type())
        .tickets(CreateBoSwimmingClassRequestFactory.tickets())
        .registrationCapacity(CreateBoSwimmingClassRequestFactory.registrationCapacity())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("담당강사는 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("강습 티켓 리스트가 null인 경우 400 반환")
  void shouldReturn400WhenTicketsIsNull() throws Exception {
    // given
    val request = CreateBoSwimmingClassRequest.builder()
        .days(CreateBoSwimmingClassRequestFactory.days())
        .time(CreateBoSwimmingClassRequestFactory.time())
        .type(CreateBoSwimmingClassRequestFactory.type())
        .instructorName("손지혜")
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
        .days(CreateBoSwimmingClassRequestFactory.days())
        .time(CreateBoSwimmingClassRequestFactory.time())
        .type(CreateBoSwimmingClassRequestFactory.type())
        .instructorName("손지혜")
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
        .days(CreateBoSwimmingClassRequestFactory.days())
        .time(CreateBoSwimmingClassRequestFactory.time())
        .type(CreateBoSwimmingClassRequestFactory.type())
        .instructorName("손지혜")
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
          .days(days())
          .time(time())
          .type(type())
          .instructorName("손지혜")
          .tickets(tickets())
          .registrationCapacity(registrationCapacity())
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
          .type(GROUP)
          .subType("마스터즈")
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