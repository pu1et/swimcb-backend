package com.project.swimcb.bo.swimmingclass.adapter.in;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.config.security.SecurityConfig;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DeleteBoSwimmingClassController.class)
@Import(SecurityConfig.class)
class DeleteBoSwimmingClassControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private static final String PATH = "/api/bo/swimming-classes";

  @Test
  @DisplayName("클래스 데이터 관리 - 클래스 일괄 수정 성공")
  void shouldUpdateSuccessfully() throws Exception {
    // given
    val request = DeleteBoSwimmingClassRequestFactory.create();
    // when
    // then
    mockMvc.perform(delete(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  private static class DeleteBoSwimmingClassRequestFactory {

    private static DeleteBoSwimmingClassRequest create() {
      return new DeleteBoSwimmingClassRequest(1L);
    }
  }
}