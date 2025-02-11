package com.project.swimcb.bo.faq.adapter.in;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.faq.adapter.in.UpdateFaqIsVisibleController;
import com.project.swimcb.bo.faq.adapter.in.UpdateFaqIsVisibleRequest;
import com.project.swimcb.config.security.SecurityConfig;
import com.project.swimcb.bo.faq.application.in.UpdateFaqIsVisibleUseCase;
import java.util.NoSuchElementException;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UpdateFaqIsVisibleController.class)
@Import(SecurityConfig.class)
class UpdateFaqIsVisibleControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UpdateFaqIsVisibleUseCase useCase;

  @Autowired
  private ObjectMapper objectMapper;

  private static final String PATH = "/api/bo/faqs/{faqId}/is-visible";

  @Test
  @DisplayName("FAQ 수정 성공")
  void shouldUpdateFaqSuccessfully() throws Exception {
    // given
    val faqId = 1L;
    val request = UpdateFaqIsVisibleRequestFactory.create();
    // when
    // then
    mockMvc.perform(put(PATH, faqId)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    verify(useCase, only()).updateFaqIsVisible(faqId, request.isVisible());
  }

  @Test
  @DisplayName("존재하지 않는 FAQ 수정 시 404 반환")
  void shouldReturn400WhenFaqNotFound() throws Exception {
    // given
    val faqId = 1L;
    val request = UpdateFaqIsVisibleRequestFactory.create();

    doThrow(NoSuchElementException.class).when(useCase).updateFaqIsVisible(anyLong(), anyBoolean());
    // when
    // then
    mockMvc.perform(put(PATH, faqId)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());

    verify(useCase, only()).updateFaqIsVisible(faqId, request.isVisible());
  }

  private static class UpdateFaqIsVisibleRequestFactory {

    public static UpdateFaqIsVisibleRequest create() {
      return new UpdateFaqIsVisibleRequest(true);
    }
  }
}