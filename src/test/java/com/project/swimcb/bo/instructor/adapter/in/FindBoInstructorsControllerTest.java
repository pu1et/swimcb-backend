package com.project.swimcb.bo.instructor.adapter.in;


import static com.project.swimcb.bo.instructor.adapter.in.FindBoInstructorsControllerTest.SWIMMING_POOL_ID;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.instructor.application.in.FindBoInstructorsUseCase;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindBoInstructorsController.class)
@WithMockTokenInfo(swimmingPoolId = SWIMMING_POOL_ID)
class FindBoInstructorsControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private FindBoInstructorsUseCase useCase;

  static final long SWIMMING_POOL_ID = 1L;

  private static final String PATH = "/api/bo/instructors";

  @Test
  @DisplayName("수영강사 조회 성공")
  void shouldFindSuccessfully() throws Exception {
    // given
    val response = TestFindSwimmingPoolBasicInfoResponseFactory.create();

    when(useCase.findBoInstructors(anyLong())).thenReturn(response);
    // when
    // then
    mockMvc.perform(get(PATH))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));

    verify(useCase, only()).findBoInstructors(SWIMMING_POOL_ID);
  }

  private static class TestFindSwimmingPoolBasicInfoResponseFactory {

    private static FindBoInstructorsResponse create() {
      return FindBoInstructorsResponse.builder()
          .instructors(
              List.of(
                  FindBoInstructorsResponse.Instructor.builder()
                      .instructorId(1L)
                      .name("DUMMY_NAME1")
                      .build(),
                  FindBoInstructorsResponse.Instructor.builder()
                      .instructorId(2L)
                      .name("DUMMY_NAME2")
                      .build()
              )
          )
          .build();
    }
  }
}