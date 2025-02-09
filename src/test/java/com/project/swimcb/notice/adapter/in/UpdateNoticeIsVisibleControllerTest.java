package com.project.swimcb.notice.adapter.in;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.config.security.SecurityConfig;
import com.project.swimcb.notice.application.in.UpdateNoticeIsVisibleUseCase;
import java.util.NoSuchElementException;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UpdateNoticeIsVisibleController.class)
@Import(SecurityConfig.class)
class UpdateNoticeIsVisibleControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UpdateNoticeIsVisibleUseCase useCase;

  @Autowired
  private ObjectMapper objectMapper;

  private final String path = "/api/notices/{noticeId}/is-visible";

  @Test
  @DisplayName("공지사항 수정 성공")
  void shouldUpdateNoticeSuccessfully() throws Exception {
    // given
    val noticeId = 1L;
    val request = UpdateNoticeIsVisibleRequestFactory.create();
    // when
    // then
    mockMvc.perform(put(path, noticeId)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    verify(useCase, only()).updateNoticeIsVisible(noticeId, request.isVisible());
  }

  @Test
  @DisplayName("존재하지 않는 공지사항 수정 시 404 반환")
  void shouldReturn400WhenNoticeNotFound() throws Exception {
    // given
    val noticeId = 1L;
    val request = UpdateNoticeIsVisibleRequestFactory.create();

    doThrow(NoSuchElementException.class).when(useCase)
        .updateNoticeIsVisible(anyLong(), anyBoolean());
    // when
    // then
    mockMvc.perform(put(path, noticeId)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());

    verify(useCase, only()).updateNoticeIsVisible(noticeId, request.isVisible());
  }

  private static class UpdateNoticeIsVisibleRequestFactory {

    public static UpdateNoticeIsVisibleRequest create() {
      return new UpdateNoticeIsVisibleRequest(true);
    }
  }
}