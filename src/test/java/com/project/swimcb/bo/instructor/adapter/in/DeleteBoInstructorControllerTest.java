package com.project.swimcb.bo.instructor.adapter.in;

import static com.project.swimcb.bo.instructor.adapter.in.DeleteBoInstructorControllerTest.SWIMMING_POOL_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.swimcb.bo.instructor.application.in.DeleteBoInstructorUseCase;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = DeleteBoInstructorController.class)
@WithMockTokenInfo(swimmingPoolId = SWIMMING_POOL_ID)
class DeleteBoInstructorControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private DeleteBoInstructorUseCase useCase;

  static final long SWIMMING_POOL_ID = 1L;

  private static final String PATH = "/api/bo/instructors/2";
  private static final long INSTRUCTOR_ID = 2L;

  @Test
  @DisplayName("강습구분 단건 삭제 성공")
  void shouldDeleteSuccessfully() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(delete(PATH))
        .andExpect(status().isOk());

    verify(useCase, only()).deleteBoInstructor(assertArg(i -> {
      assertThat(i.swimmingPoolId()).isEqualTo(SWIMMING_POOL_ID);
      assertThat(i.instructorId()).isEqualTo(INSTRUCTOR_ID);
    }));
  }
}