package com.project.swimcb.bo.swimmingclass.adapter.in;

import static com.project.swimcb.bo.swimmingclass.adapter.in.CreateBoSwimmingClassSubTypeControllerTest.SWIMMING_POOL_ID;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.swimmingclass.application.in.CreateBoSwimmingClassSubTypeUseCase;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = CreateBoSwimmingClassSubTypeController.class)
@WithMockTokenInfo(swimmingPoolId = SWIMMING_POOL_ID)
class CreateBoSwimmingClassSubTypeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private CreateBoSwimmingClassSubTypeUseCase createBoSwimmingClassSubTypeUseCase;

  private static final String PATH = "/api/bo/swimming-classes/class-sub-types";
  static final long SWIMMING_POOL_ID = 1L;

  @Test
  @DisplayName("강습구분 단건 생성 성공")
  void shouldCreateSuccessfully() throws Exception {
    // given
    val request = new CreateBoSwimmingClassSubTypeRequest(1L, "DUMMY_NAME");
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    verify(createBoSwimmingClassSubTypeUseCase, only()).createBoSwimmingClassSubType(
        request.toCommand(SWIMMING_POOL_ID));
  }

  @Test
  @DisplayName("request가 null일 경우 400 반환")
  void shouldReturn400WhenRequestIsNull() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("강습구분 이름이 null일 경우 400 반환")
  void shouldReturn400WhenTypeNameIsNull() throws Exception {
    // given
    val request = new CreateBoSwimmingClassSubTypeRequest(1L, null);
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습구분 이름은 null이 될 수 없습니다.")));
  }
}