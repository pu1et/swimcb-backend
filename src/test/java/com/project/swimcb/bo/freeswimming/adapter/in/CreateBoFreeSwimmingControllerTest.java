package com.project.swimcb.bo.freeswimming.adapter.in;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.freeswimming.adapter.in.CreateBoFreeSwimmingRequest.Days;
import com.project.swimcb.bo.freeswimming.adapter.in.CreateBoFreeSwimmingRequest.RegistrationCapacity;
import com.project.swimcb.bo.freeswimming.adapter.in.CreateBoFreeSwimmingRequest.Ticket;
import com.project.swimcb.bo.freeswimming.adapter.in.CreateBoFreeSwimmingRequest.Time;
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

@WebMvcTest(CreateBoFreeSwimmingController.class)
@Import(SecurityConfig.class)
class CreateBoFreeSwimmingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private static final String PATH = "/api/bo/free-swimming";

  @Test
  @DisplayName("자유수영 데이터 관리 - 자유수영 추가 성공")
  void shouldCreateSuccessfully() throws Exception {
    // given
    val request = CreateBoFreeSwimmingRequestFactory.create();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("자유수영 요일이 null인 경우 400 반환")
  void shouldReturn400WhenDayIsNull() throws Exception {
    // given
    val request = CreateBoFreeSwimmingRequest.builder()
        .time(CreateBoFreeSwimmingRequestFactory.time())
        .instructorName("손지혜")
        .tickets(CreateBoFreeSwimmingRequestFactory.tickets())
        .registrationCapacity(
            CreateBoFreeSwimmingRequestFactory.registrationCapacity())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("자유수영 요일은 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("자유수영 시간이 null인 경우 400 반환")
  void shouldReturn400WhenTimeIsNull() throws Exception {
    // given
    val request = CreateBoFreeSwimmingRequest.builder()
        .days(CreateBoFreeSwimmingRequestFactory.days())
        .instructorName("손지혜")
        .tickets(CreateBoFreeSwimmingRequestFactory.tickets())
        .registrationCapacity(
            CreateBoFreeSwimmingRequestFactory.registrationCapacity())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("자유수영 시작/종료 시간은 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("자유수영 시작 시간이 null인 경우 400 반환")
  void shouldReturn400WhenStartTimeIsNull() throws Exception {
    // given
    val request = CreateBoFreeSwimmingRequest.builder()
        .days(CreateBoFreeSwimmingRequestFactory.days())
        .time(Time.builder().endTime(LocalTime.of(6, 50)).build())
        .instructorName("손지혜")
        .tickets(CreateBoFreeSwimmingRequestFactory.tickets())
        .registrationCapacity(
            CreateBoFreeSwimmingRequestFactory.registrationCapacity())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("자유수영 시작 시간은 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("자유수영 종료 시간이 null인 경우 400 반환")
  void shouldReturn400WhenEndTimeIsNull() throws Exception {
    // given
    val request = CreateBoFreeSwimmingRequest.builder()
        .days(CreateBoFreeSwimmingRequestFactory.days())
        .time(Time.builder().startTime(LocalTime.of(6, 0)).build())
        .instructorName("손지혜")
        .tickets(CreateBoFreeSwimmingRequestFactory.tickets())
        .registrationCapacity(
            CreateBoFreeSwimmingRequestFactory.registrationCapacity())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("자유수영 종료 시간은 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("담당강사가 null인 경우 400 반환")
  void shouldReturn400WhenInstructorIsNull() throws Exception {
    // given
    val request = CreateBoFreeSwimmingRequest.builder()
        .days(CreateBoFreeSwimmingRequestFactory.days())
        .time(CreateBoFreeSwimmingRequestFactory.time())
        .tickets(CreateBoFreeSwimmingRequestFactory.tickets())
        .registrationCapacity(
            CreateBoFreeSwimmingRequestFactory.registrationCapacity())
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
  @DisplayName("자유수영 티켓 리스트가 null인 경우 400 반환")
  void shouldReturn400WhenTicketsIsNull() throws Exception {
    // given
    val request = CreateBoFreeSwimmingRequest.builder()
        .days(CreateBoFreeSwimmingRequestFactory.days())
        .time(CreateBoFreeSwimmingRequestFactory.time())
        .instructorName("손지혜")
        .registrationCapacity(
            CreateBoFreeSwimmingRequestFactory.registrationCapacity())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("자유수영 티켓 리스트는 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("티켓 이름이 null인 경우 400 반환")
  void shouldReturn400WhenTicketNameIsNull() throws Exception {
    // given
    val request = CreateBoFreeSwimmingRequest.builder()
        .days(CreateBoFreeSwimmingRequestFactory.days())
        .time(CreateBoFreeSwimmingRequestFactory.time())
        .instructorName("손지혜")
        .tickets(List.of(Ticket.builder().price(100000).build()))
        .registrationCapacity(
            CreateBoFreeSwimmingRequestFactory.registrationCapacity())
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
    val request = CreateBoFreeSwimmingRequest.builder()
        .days(CreateBoFreeSwimmingRequestFactory.days())
        .time(CreateBoFreeSwimmingRequestFactory.time())
        .instructorName("손지혜")
        .tickets(CreateBoFreeSwimmingRequestFactory.tickets())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("정원 정보는 null이 될 수 없습니다.")));
  }

  private static class CreateBoFreeSwimmingRequestFactory {

    private static CreateBoFreeSwimmingRequest create() {
      return CreateBoFreeSwimmingRequest.builder()
          .days(days())
          .time(time())
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