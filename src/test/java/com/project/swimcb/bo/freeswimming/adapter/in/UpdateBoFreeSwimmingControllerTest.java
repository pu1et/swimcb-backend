package com.project.swimcb.bo.freeswimming.adapter.in;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = UpdateBoFreeSwimmingController.class)
class UpdateBoFreeSwimmingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private final String PATH = "/api/bo/free-swimming/1";

  @Nested
  @DisplayName("요청 검증")
  class DescribeRequestValidation {

    @Nested
    @DisplayName("필수 필드가 누락된 경우")
    class ContextWithMissingRequiredFields {

      @Test
      @DisplayName("yearMonth가 null이면 400 에러를 반환한다")
      void shouldReturn400WhenYearMonthIsNull() throws Exception {
        // given
        val request = UpdateBoFreeSwimmingRequestFactory.createWithNullYearMonth();

        // when & then
        mockMvc.perform(put(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("년/월은 null이 될 수 없습니다")));
      }

      @Test
      @DisplayName("days가 null이면 400 에러를 반환한다")
      void shouldReturn400WhenDaysIsNull() throws Exception {
        // given
        val request = UpdateBoFreeSwimmingRequestFactory.createWithNullDays();

        // when & then
        mockMvc.perform(put(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("자유수영 요일은 필수입니다")));
      }

      @Test
      @DisplayName("time이 null이면 400 에러를 반환한다")
      void shouldReturn400WhenTimeIsNull() throws Exception {
        // given
        val request = UpdateBoFreeSwimmingRequestFactory.createWithNullTime();

        // when & then
        mockMvc.perform(put(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("자유수영 시작/종료 시간은 null이 될 수 없습니다")));
      }

      @Test
      @DisplayName("startTime이 null이면 400 에러를 반환한다")
      void shouldReturn400WhenStartTimeIsNull() throws Exception {
        // given
        val request = UpdateBoFreeSwimmingRequestFactory.createWithNullStartTime();

        // when & then
        mockMvc.perform(put(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("자유수영 시작 시간은 null이 될 수 없습니다")));
      }

      @Test
      @DisplayName("endTime이 null이면 400 에러를 반환한다")
      void shouldReturn400WhenEndTimeIsNull() throws Exception {
        // given
        val request = UpdateBoFreeSwimmingRequestFactory.createWithNullEndTime();

        // when & then
        mockMvc.perform(put(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("자유수영 종료 시간은 null이 될 수 없습니다")));
      }

      @Test
      @DisplayName("tickets가 null이면 400 에러를 반환한다")
      void shouldReturn400WhenTicketsIsNull() throws Exception {
        // given
        val request = UpdateBoFreeSwimmingRequestFactory.createWithNullTickets();

        // when & then
        mockMvc.perform(put(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("자유수영 티켓 리스트는 필수입니다")));
      }

      @DisplayName("ticket name이 null이면 400 에러를 반환한다")
      void shouldReturn400WhenTicketNameIsNull() throws Exception {
        // given
        val request = UpdateBoFreeSwimmingRequestFactory.createWithNullTicketName();

        // when & then
        mockMvc.perform(put(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("티켓 이름은 필수입니다")));
      }

      @Test
      @DisplayName("tickets price가 null이면 400 에러를 반환한다")
      void shouldReturn400WhenTicketPriceIsNull() throws Exception {
        // given
        val request = UpdateBoFreeSwimmingRequestFactory.createWithNullTicketPrice();

        // when & then
        mockMvc.perform(put(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("티켓 가격은 null이 될 수 없습니다")));
      }

      @Test
      @DisplayName("capacity가 null이면 400 에러를 반환한다")
      void shouldReturn400WhenCapacityIsNull() throws Exception {
        // given
        val request = UpdateBoFreeSwimmingRequestFactory.createWithNullCapacity();

        // when & then
        mockMvc.perform(put(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("정원 정보는 null이 될 수 없습니다")));
      }

      @Test
      @DisplayName("isExposed가 null이면 400 에러를 반환한다")
      void shouldReturn400WhenIsExposedIsNull() throws Exception {
        // given
        val request = UpdateBoFreeSwimmingRequestFactory.createWithNullIsExposed();

        // when & then
        mockMvc.perform(put(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("사용자 노출 여부는 null이 될 수 없습니다")));
      }

    }

    @Nested
    @DisplayName("필드 값이 유효하지 않은 경우")
    class ContextWithInvalidFieldValues {

      @Test
      @DisplayName("days가 빈 리스트이면 400 에러를 반환한다")
      void shouldReturn400WhenDaysIsEmpty() throws Exception {
        // given
        val request = UpdateBoFreeSwimmingRequestFactory.createWithEmptyDays();

        // when & then
        mockMvc.perform(put(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("자유수영 요일은 필수입니다")));
      }

      @Test
      @DisplayName("tickets가 빈 리스트이면 400 에러를 반환한다")
      void shouldReturn400WhenTicketsIsEmpty() throws Exception {
        // given
        val request = UpdateBoFreeSwimmingRequestFactory.createWithEmptyTickets();

        // when & then
        mockMvc.perform(put(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("자유수영 티켓 리스트는 필수입니다")));
      }

      @Test
      @DisplayName("capacity가 0 미만이면 400 에러를 반환한다")
      void shouldReturn400WhenCapacityIsZeroOrNegative() throws Exception {
        // given
        val request = UpdateBoFreeSwimmingRequestFactory.createWithInvalidCapacity(-1);

        // when & then
        mockMvc.perform(put(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("정원 정보는 0 이상이어야 합니다")));
      }

    }

    @Nested
    @DisplayName("티켓 정보가 유효하지 않은 경우")
    class ContextWithInvalidTicketInfo {

      @Test
      @DisplayName("티켓 이름이 공백이면 400 에러를 반환한다")
      void shouldReturn400WhenTicketNameIsBlank() throws Exception {
        // given
        val request = UpdateBoFreeSwimmingRequestFactory.createWithBlankTicketName();

        // when & then
        mockMvc.perform(put(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("티켓 이름은 필수입니다")));
      }

      @Test
      @DisplayName("티켓 가격이 0 미만이면 400 에러를 반환한다")
      void shouldReturn400WhenTicketPriceIsZeroOrNegative() throws Exception {
        // given
        val request = UpdateBoFreeSwimmingRequestFactory.createWithInvalidTicketPrice(-1);

        // when & then
        mockMvc.perform(put(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("티켓 가격은 0 이상이어야 합니다")));
      }

    }

  }

  private static class UpdateBoFreeSwimmingRequestFactory {

    public static UpdateBoFreeSwimmingRequest createWithNullYearMonth() {
      return createValid()
          .yearMonth(null)
          .build();
    }

    private static UpdateBoFreeSwimmingRequest createWithNullDays() {
      return createValid()
          .days(null)
          .build();
    }

    private static UpdateBoFreeSwimmingRequest createWithNullTime() {
      return createValid()
          .time(null)
          .build();
    }

    private static UpdateBoFreeSwimmingRequest createWithNullTickets() {
      return createValid()
          .tickets(null)
          .build();
    }

    public static UpdateBoFreeSwimmingRequest createWithNullTicketName() {
      return createValid()
          .tickets(List.of(UpdateBoFreeSwimmingRequest.Ticket.builder()
              .name(null)
              .price(10000)
              .build()))
          .build();
    }

    private static UpdateBoFreeSwimmingRequest createWithNullTicketPrice() {
      return createValid()
          .tickets(List.of(UpdateBoFreeSwimmingRequest.Ticket.builder()
              .name("성인일반")
              .price(null)
              .build()))
          .build();
    }

    public static UpdateBoFreeSwimmingRequest createWithNullCapacity() {
      return createValid()
          .capacity(null)
          .build();
    }

    private static UpdateBoFreeSwimmingRequest createWithNullIsExposed() {
      return createValid()
          .isExposed(null)
          .build();
    }

    private static UpdateBoFreeSwimmingRequest createWithEmptyDays() {
      return createValid()
          .days(List.of())
          .build();
    }

    private static UpdateBoFreeSwimmingRequest createWithEmptyTickets() {
      return createValid()
          .tickets(List.of())
          .build();
    }

    private static UpdateBoFreeSwimmingRequest createWithInvalidCapacity(int capacity) {
      return createValid()
          .capacity(capacity)
          .build();
    }

    private static UpdateBoFreeSwimmingRequest createWithBlankTicketName() {
      return createValid()
          .tickets(List.of(UpdateBoFreeSwimmingRequest.Ticket.builder()
              .name("")
              .price(10000)
              .build()))
          .build();
    }

    private static UpdateBoFreeSwimmingRequest createWithInvalidTicketPrice(int price) {
      return createValid()
          .tickets(List.of(UpdateBoFreeSwimmingRequest.Ticket.builder()
              .name("성인일반")
              .price(price)
              .build()))
          .build();
    }

    private static UpdateBoFreeSwimmingRequest createWithNullStartTime() {
      return createValid()
          .time(UpdateBoFreeSwimmingRequest.Time.builder()
              .startTime(null)
              .endTime(LocalTime.of(10, 0))
              .build())
          .build();
    }

    private static UpdateBoFreeSwimmingRequest createWithNullEndTime() {
      return createValid()
          .time(UpdateBoFreeSwimmingRequest.Time.builder()
              .startTime(LocalTime.of(9, 0))
              .endTime(null)
              .build())
          .build();
    }

    private static UpdateBoFreeSwimmingRequest.UpdateBoFreeSwimmingRequestBuilder createValid() {
      return UpdateBoFreeSwimmingRequest.builder()
          .yearMonth(YearMonth.of(2025, 10))
          .days(List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY))
          .time(UpdateBoFreeSwimmingRequest.Time.builder()
              .startTime(LocalTime.of(9, 0))
              .endTime(LocalTime.of(10, 0))
              .build())
          .lifeguardId(1L)
          .tickets(List.of(UpdateBoFreeSwimmingRequest.Ticket.builder()
              .name("성인일반")
              .price(10000)
              .build()))
          .capacity(20)
          .isExposed(true);
    }

  }

}
