package com.project.swimcb.bo.instructor.adapter.in;

import static com.project.swimcb.bo.instructor.adapter.in.UpdateBoInstructorControllerTest.SWIMMING_POOL_ID;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.instructor.application.in.UpdateBoInstructorUseCase;
import com.project.swimcb.bo.swimmingclass.adapter.in.CreateBoSwimmingClassSubTypeRequest;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = UpdateBoInstructorController.class)
@WithMockTokenInfo(swimmingPoolId = SWIMMING_POOL_ID)
class UpdateBoInstructorControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private UpdateBoInstructorUseCase useCase;

  static final long SWIMMING_POOL_ID = 1L;

  private static final String PATH = "/api/bo/instructors/1";
  private static final long INSTRUCTOR_ID = 1L;

  @Test
  @DisplayName("강사 단건 업데이트 성공")
  void shouldUpdateSuccessfully() throws Exception {
    // given
    val request = new UpdateBoInstructorRequest("DUMMY_NAME");
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    verify(useCase, only()).updateBoInstructor(
        request.toCommand(SWIMMING_POOL_ID, INSTRUCTOR_ID));
  }

  @Test
  @DisplayName("request가 null일 경우 400 반환")
  void shouldReturn400WhenRequestIsNull() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("강사 이름이 null일 경우 400 반환")
  void shouldReturn400WhenInstructorNameIsNull() throws Exception {
    // given
    val request = new UpdateBoInstructorRequest(null);
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강사 이름은 null이 될 수 없습니다.")));
  }
}