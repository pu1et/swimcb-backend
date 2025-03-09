package com.project.swimcb.bo.faq.adapter.in;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.faq.application.in.UpdateFaqUseCase;
import com.project.swimcb.bo.faq.domain.UpdateFaqCommand;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.config.security.JwtAuthenticationFilter;
import com.project.swimcb.config.security.SecurityConfig;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = UpdateFaqController.class)
class UpdateFaqControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UpdateFaqUseCase useCase;

  @Autowired
  private ObjectMapper objectMapper;

  private static final String PATH = "/api/bo/faqs/{faqId}";

  @Test
  @DisplayName("FAQ 수정 성공")
  void shouldUpdateFaqSuccessfully() throws Exception {
    // given
    val faqId = 1L;
    val request = UpdateFaqRequestFactory.create();
    // when
    // then
    mockMvc.perform(put(PATH, faqId)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    verify(useCase, only()).updateFaq(UpdateFaqCommand.from(faqId, request));
  }

  @Test
  @DisplayName("존재하지 않는 FAQ 수정 시 404 반환")
  void shouldReturn400WhenFaqNotFound() throws Exception {
    // given
    val faqId = 1L;
    val request = UpdateFaqRequestFactory.create();

    doThrow(NoSuchElementException.class).when(useCase).updateFaq(any());
    // when
    // then
    mockMvc.perform(put(PATH, faqId)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());

    verify(useCase, only()).updateFaq(UpdateFaqCommand.from(faqId, request));
  }

  @Test
  @DisplayName("FAQ 수정 시 제목이 없으면 400 반환")
  void shouldReturn400WhenTitleIsMissing() throws Exception {
    // given
    val faqId = 1L;
    val request = UpdateFaqRequestFactory.emptyTitle();
    // when
    // then
    mockMvc.perform(put(PATH, faqId)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("FAQ 제목은 null이 아니어야 합니다.")));

    verify(useCase, never()).updateFaq(any());
  }

  @Test
  @DisplayName("FAQ 수정 시 내용이 없으면 400 반환")
  void shouldReturn400WhenContentIsMissing() throws Exception {
    // given
    val faqId = 1L;
    val request = UpdateFaqRequestFactory.emptyContent();
    // when
    // then
    mockMvc.perform(put(PATH, faqId)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("FAQ 내용은 null이 아니어야 합니다.")));

    verify(useCase, never()).updateFaq(any());
  }

  @Test
  @DisplayName("FAQ 수정 시 이미지 리스트가 없으면 400 반환")
  void shouldReturn400WhenImageUrlsIsMissing() throws Exception {
    // given
    val faqId = 1L;
    val request = UpdateFaqRequestFactory.emptyImageUrls();
    // when
    // then
    mockMvc.perform(put(PATH, faqId)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("FAQ 이미지 URL 리스트는 null이 아니어야 합니다.")));

    verify(useCase, never()).updateFaq(any());
  }

  private static class UpdateFaqRequestFactory {

    private static UpdateFaqRequest create() {
      return new UpdateFaqRequest("title", "content", List.of("image"), true);
    }

    private static UpdateFaqRequest emptyTitle() {
      return new UpdateFaqRequest(null, "content", List.of("image"), true);
    }

    private static UpdateFaqRequest emptyContent() {
      return new UpdateFaqRequest("title", null, List.of("image"), true);
    }

    private static UpdateFaqRequest emptyImageUrls() {
      return new UpdateFaqRequest("title", "content", null, true);
    }
  }
}