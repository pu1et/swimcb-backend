package com.project.swimcb.bo.freeswimming.adapter.in;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

@WebMvcTest(UpdateBoFreeSwimmingImageController.class)
@Import(SecurityConfig.class)
class UpdateBoFreeSwimmingImageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private final String PATH = "/api/bo/free-swimming/images";

  @Test
  @DisplayName("자유수영 이미지 업데이트 성공")
  void shouldUpdateFreeSwimmingImageSuccessfully() throws Exception {
    // given
    val request = UpdateFreeSwimmingImageRequestFactory.create();
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("imagePath가 null이면 400 반환")
  void shouldReturn400WhenImagePathIsNull() throws Exception {
    // given
    val request = UpdateFreeSwimmingImageRequestFactory.imageIsNull();
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("imagePath는 null일 수 없습니다.")));
  }

  private static class UpdateFreeSwimmingImageRequestFactory {

    private static UpdateBoFreeSwimmingImageRequest create() {
      return new UpdateBoFreeSwimmingImageRequest("/free-swimming/image.jpg");
    }

    private static UpdateBoFreeSwimmingImageRequest imageIsNull() {
      return new UpdateBoFreeSwimmingImageRequest(null);
    }
  }
}