package com.project.swimcb.bo.freeswimming.adapter.in;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.freeswimming.adapter.in.CreateBoFreeSwimmingRequest.RegistrationCapacity;
import com.project.swimcb.bo.freeswimming.adapter.in.CreateBoFreeSwimmingRequest.Ticket;
import com.project.swimcb.bo.freeswimming.adapter.in.CreateBoFreeSwimmingRequest.Time;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = CreateBoFreeSwimmingController.class)
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
  @DisplayName("담당강사 ID가 null이어도 자유수영 추가 성공")
  void shouldCreateSuccessfullyWithNullInstructorId() throws Exception {
    // given
    val request = CreateBoFreeSwimmingRequest.builder()
        .month(CreateBoFreeSwimmingRequestFactory.month())
        .days(CreateBoFreeSwimmingRequestFactory.days())
        .time(CreateBoFreeSwimmingRequestFactory.time())
        .instructorId(null) // 담당강사 이름이 null인 경우
        .tickets(CreateBoFreeSwimmingRequestFactory.tickets())
        .registrationCapacity(
            CreateBoFreeSwimmingRequestFactory.registrationCapacity())
        .isExposed(CreateBoFreeSwimmingRequestFactory.isExposed())
        .build();
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
        .month(CreateBoFreeSwimmingRequestFactory.month())
        .days(null) // 요일이 null인 경우
        .time(CreateBoFreeSwimmingRequestFactory.time())
        .tickets(CreateBoFreeSwimmingRequestFactory.tickets())
        .registrationCapacity(
            CreateBoFreeSwimmingRequestFactory.registrationCapacity())
        .isExposed(CreateBoFreeSwimmingRequestFactory.isExposed())
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
        .month(CreateBoFreeSwimmingRequestFactory.month())
        .days(CreateBoFreeSwimmingRequestFactory.days())
        .time(null) // 시간이 null인 경우
        .tickets(CreateBoFreeSwimmingRequestFactory.tickets())
        .registrationCapacity(
            CreateBoFreeSwimmingRequestFactory.registrationCapacity())
        .isExposed(CreateBoFreeSwimmingRequestFactory.isExposed())
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
        .month(CreateBoFreeSwimmingRequestFactory.month())
        .days(CreateBoFreeSwimmingRequestFactory.days())
        .time(Time.builder().endTime(LocalTime.of(6, 50)).build())
        .tickets(CreateBoFreeSwimmingRequestFactory.tickets())
        .registrationCapacity(
            CreateBoFreeSwimmingRequestFactory.registrationCapacity())
        .isExposed(CreateBoFreeSwimmingRequestFactory.isExposed())
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
        .month(CreateBoFreeSwimmingRequestFactory.month())
        .days(CreateBoFreeSwimmingRequestFactory.days())
        .time(Time.builder().startTime(LocalTime.of(6, 0)).build())
        .tickets(CreateBoFreeSwimmingRequestFactory.tickets())
        .registrationCapacity(
            CreateBoFreeSwimmingRequestFactory.registrationCapacity())
        .isExposed(CreateBoFreeSwimmingRequestFactory.isExposed())
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
  @DisplayName("자유수영 티켓 리스트가 null인 경우 400 반환")
  void shouldReturn400WhenTicketsIsNull() throws Exception {
    // given
    val request = CreateBoFreeSwimmingRequest.builder()
        .month(CreateBoFreeSwimmingRequestFactory.month())
        .days(CreateBoFreeSwimmingRequestFactory.days())
        .time(CreateBoFreeSwimmingRequestFactory.time())
        .tickets(null) // 티켓 리스트가 null인 경우
        .registrationCapacity(
            CreateBoFreeSwimmingRequestFactory.registrationCapacity())
        .isExposed(CreateBoFreeSwimmingRequestFactory.isExposed())
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
        .month(CreateBoFreeSwimmingRequestFactory.month())
        .days(CreateBoFreeSwimmingRequestFactory.days())
        .time(CreateBoFreeSwimmingRequestFactory.time())
        .tickets(List.of(Ticket.builder().price(100000).build()))
        .registrationCapacity(
            CreateBoFreeSwimmingRequestFactory.registrationCapacity())
        .isExposed(CreateBoFreeSwimmingRequestFactory.isExposed())
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
  @DisplayName("티켓 가격이 null인 경우 400 반환")
  void shouldReturn400WhenTicketPriceIsNull() throws Exception {
    // given
    val request = CreateBoFreeSwimmingRequest.builder()
        .month(CreateBoFreeSwimmingRequestFactory.month())
        .days(CreateBoFreeSwimmingRequestFactory.days())
        .time(CreateBoFreeSwimmingRequestFactory.time())
        .tickets(List.of(Ticket.builder().price(100000).build()))
        .registrationCapacity(
            CreateBoFreeSwimmingRequestFactory.registrationCapacity())
        .isExposed(CreateBoFreeSwimmingRequestFactory.isExposed())
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
        .month(CreateBoFreeSwimmingRequestFactory.month())
        .days(CreateBoFreeSwimmingRequestFactory.days())
        .time(CreateBoFreeSwimmingRequestFactory.time())
        .tickets(CreateBoFreeSwimmingRequestFactory.tickets())
        .registrationCapacity(null) // 정원 정보가 null인 경우
        .isExposed(CreateBoFreeSwimmingRequestFactory.isExposed())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("정원 정보는 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("총 정원이 null인 경우 400 반환")
  void shouldReturn400WhenTotalCapacityIsNull() throws Exception {
    // given
    val request = CreateBoFreeSwimmingRequest.builder()
        .month(CreateBoFreeSwimmingRequestFactory.month())
        .days(CreateBoFreeSwimmingRequestFactory.days())
        .time(CreateBoFreeSwimmingRequestFactory.time())
        .tickets(CreateBoFreeSwimmingRequestFactory.tickets())
        .registrationCapacity(
            RegistrationCapacity.builder()
                .totalCapacity(null)
                .reservationLimitCount(11) // 총 정원이 null인 경우
                .build()
        )
        .isExposed(CreateBoFreeSwimmingRequestFactory.isExposed())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("총 정원은 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("예약 허용 정원이 null인 경우 400 반환")
  void shouldReturn400WhenReservationLimitCountIsNull() throws Exception {
    // given
    val request = CreateBoFreeSwimmingRequest.builder()
        .month(CreateBoFreeSwimmingRequestFactory.month())
        .days(CreateBoFreeSwimmingRequestFactory.days())
        .time(CreateBoFreeSwimmingRequestFactory.time())
        .tickets(CreateBoFreeSwimmingRequestFactory.tickets())
        .registrationCapacity(
            RegistrationCapacity.builder()
                .totalCapacity(20)
                .reservationLimitCount(null) // 예약 허용 정원이 null인 경우
                .build()
        )
        .isExposed(CreateBoFreeSwimmingRequestFactory.isExposed())
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("예약 허용 인원은 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("사용자 노출 여부가 null인 경우 400 반환")
  void shouldReturn400WhenIsExposedIsNull() throws Exception {
    // given
    val request = CreateBoFreeSwimmingRequest.builder()
        .month(CreateBoFreeSwimmingRequestFactory.month())
        .days(CreateBoFreeSwimmingRequestFactory.days())
        .time(CreateBoFreeSwimmingRequestFactory.time())
        .registrationCapacity(CreateBoFreeSwimmingRequestFactory.registrationCapacity())
        .tickets(CreateBoFreeSwimmingRequestFactory.tickets())
        .isExposed(null) // 사용자 노출 여부가 null인 경우
        .build();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("사용자 노출 여부는 null이 될 수 없습니다.")));
  }


  private static class CreateBoFreeSwimmingRequestFactory {

    private static CreateBoFreeSwimmingRequest create() {
      return CreateBoFreeSwimmingRequest.builder()
          .month(month())
          .days(days())
          .time(time())
          .instructorId(instructorId())
          .tickets(tickets())
          .registrationCapacity(registrationCapacity())
          .isExposed(isExposed())
          .build();
    }

    private static int month() {
      return 1;
    }

    private static List<DayOfWeek> days() {
      return List.of(MONDAY, WEDNESDAY);
    }

    private static Time time() {
      return Time.builder()
          .startTime(LocalTime.of(6, 0))
          .endTime(LocalTime.of(6, 50))
          .build();
    }

    private static Long instructorId() {
      return 1L;
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

    private static Boolean isExposed() {
      return true;
    }

  }

}
