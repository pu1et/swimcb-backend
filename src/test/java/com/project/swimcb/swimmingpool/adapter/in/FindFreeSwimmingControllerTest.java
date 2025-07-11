package com.project.swimcb.swimmingpool.adapter.in;

import static com.project.swimcb.swimmingpool.adapter.in.FindFreeSwimmingControllerTest.MEMBER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.instructor.adapter.in.FindBoInstructorsController;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import com.project.swimcb.swimmingpool.application.in.FindFreeSwimmingUseCase;
import com.project.swimcb.swimmingpool.domain.FindFreeSwimmingResponse;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindFreeSwimmingController.class)
@WithMockTokenInfo(memberId = MEMBER_ID)
class FindFreeSwimmingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private FindFreeSwimmingUseCase findFreeSwimmingUseCase;

  @MockitoBean
  private FindFreeSwimmingResponseMapper findFreeSwimmingResponseMapper;

  @Autowired
  private ObjectMapper objectMapper;

  static final long MEMBER_ID = 1L;

  private final String PATH = "/api/free-swimming";

  @Test
  @DisplayName("자유수영 조회 시 올바른 응답을 반환해야 한다.")
  void shouldReturnOkWithValidResponse() throws Exception {
    // given
    val response = new FindFreeSwimmingResponse(List.of());

    given(findFreeSwimmingUseCase.findFreeSwimming(any())).willReturn(List.of());
    given(findFreeSwimmingResponseMapper.toResponse(any())).willReturn(response);

    // when
    // then
    mockMvc.perform(get(PATH)
            .param("memberLatitude", "37.0")
            .param("memberLongitude", "126.0")
            .param("minLatitude", "37.0")
            .param("maxLatitude", "38.0")
            .param("minLongitude", "126.0")
            .param("maxLongitude", "127.0")
            .param("isTodayAvailable", "true")
            .param("date", "2024-12-31")
            .param("start-times", "09:00", "10:00"))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));
  }

  @Test
  @DisplayName("자유수영 조회 시 날짜가 없는 경우에도 올바른 응답을 반환해야 한다.")
  void shouldReturnOkWithoutDate() throws Exception {
    // given
    when(findFreeSwimmingUseCase.findFreeSwimming(any()))
        .thenReturn(List.of());
    when(findFreeSwimmingResponseMapper.toResponse(any()))
        .thenReturn(new FindFreeSwimmingResponse(List.of()));

    // when
    // then
    mockMvc.perform(get(PATH)
            .param("memberLatitude", "37.0")
            .param("memberLongitude", "126.0")
            .param("minLatitude", "37.0")
            .param("maxLatitude", "38.0")
            .param("minLongitude", "126.0")
            .param("maxLongitude", "127.0")
            .param("isTodayAvailable", "true")
            .param("start-times", "09:00", "10:00"))
        .andExpect(status().isOk());
  }

  @Nested
  @DisplayName("파라미터 검증")
  class ParameterValidation {

    @Test
    @DisplayName("잘못된 위도/경도 값이 주어지면 400 Bad Request를 반환해야 한다.")
    void shouldReturnBadRequestForInvalidCoordinates() throws Exception {
      mockMvc.perform(get(PATH)
              .param("memberLatitude", "invalid")
              .param("memberLongitude", "126.0")
              .param("minLatitude", "37.0")
              .param("maxLatitude", "38.0")
              .param("minLongitude", "126.0")
              .param("maxLongitude", "127.0")
              .param("isTodayAvailable", "true"))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("잘못된 날짜 형식이 주어지면 400 Bad Request를 반환해야 한다.")
    void shouldReturnBadRequestForInvalidDate() throws Exception {
      mockMvc.perform(get(PATH)
              .param("memberLatitude", "37.0")
              .param("memberLongitude", "126.0")
              .param("minLatitude", "37.0")
              .param("maxLatitude", "38.0")
              .param("minLongitude", "126.0")
              .param("maxLongitude", "127.0")
              .param("isTodayAvailable", "true")
              .param("date", "17:00"))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("잘못된 시간 형식이 주어지면 400 Bad Request를 반환해야 한다.")
    void shouldReturnBadRequestForInvalidStartTimes() throws Exception {
      mockMvc.perform(get(PATH)
              .param("memberLatitude", "37.0")
              .param("memberLongitude", "126.0")
              .param("minLatitude", "37.0")
              .param("maxLatitude", "38.0")
              .param("minLongitude", "126.0")
              .param("maxLongitude", "127.0")
              .param("isTodayAvailable", "true")
              .param("start-times", "2025-01-01"))
          .andExpect(status().isBadRequest());
    }

  }


}
