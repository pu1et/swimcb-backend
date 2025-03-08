package com.project.swimcb.bo.instructor.adapter.in;

import static com.project.swimcb.bo.instructor.adapter.in.CreateBoInstructorControllerTest.SWIMMING_POOL_ID;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.instructor.application.in.CreateBoInstructorUseCase;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = CreateBoInstructorController.class)
@WithMockTokenInfo(swimmingPoolId = SWIMMING_POOL_ID)
class CreateBoInstructorControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private CreateBoInstructorUseCase useCase;

  static final long SWIMMING_POOL_ID = 1L;

  private static final String PATH = "/api/bo/instructors";

  @Test
  @DisplayName("강사 리스트 단건 생성 성공")
  void shouldUpdateSuccessfully() throws Exception {
    // given
    val request = UpdateBoSwimmingClassInstructorsRequestFactory.create();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    verify(useCase, only()).createBoInstructor(request.toCommand(SWIMMING_POOL_ID));
  }

  @Test
  @DisplayName("request가 null일 경우 400 반환")
  void shouldReturn400WhenRequestIsNull() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(post(PATH))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("강사 이름이 null일 경우 400 반환")
  void shouldReturn400WhenInstructorNameIsNull() throws Exception {
    // given
    val request = UpdateBoSwimmingClassInstructorsRequestFactory.instructorNameIsNull();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강사 이름은 null이 될 수 없습니다.")));
  }

  private static class UpdateBoSwimmingClassInstructorsRequestFactory {

    private static CreateBoInstructorRequest create() {
      return CreateBoInstructorRequest.builder()
          .name("김민수")
          .build();
    }

    private static CreateBoInstructorRequest instructorNameIsNull() {
      return CreateBoInstructorRequest.builder().build();
    }
  }
}