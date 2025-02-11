package com.project.swimcb.notice.adapter.in;

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
import com.project.swimcb.config.security.SecurityConfig;
import com.project.swimcb.notice.application.in.UpdateNoticeUseCase;
import com.project.swimcb.notice.domain.UpdateNoticeCommand;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UpdateNoticeController.class)
@Import(SecurityConfig.class)
class UpdateNoticeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UpdateNoticeUseCase useCase;

  @Autowired
  private ObjectMapper objectMapper;

  private static final String path = "/api/notices/{noticeId}";

  @Test
  @DisplayName("공지사항 수정 성공")
  void shouldUpdateNoticeSuccessfully() throws Exception {
    // given
    val noticeId = 1L;
    val request = UpdateNoticeRequestFactory.create();
    // when
    // then
    mockMvc.perform(put(path, noticeId)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    verify(useCase, only()).updateNotice(UpdateNoticeCommand.from(noticeId, request));
  }

  @Test
  @DisplayName("존재하지 않는 공지사항 수정 시 404 반환")
  void shouldReturn400WhenNoticeNotFound() throws Exception {
    // given
    val noticeId = 1L;
    val request = UpdateNoticeRequestFactory.create();

    doThrow(NoSuchElementException.class).when(useCase).updateNotice(any());
    // when
    // then
    mockMvc.perform(put(path, noticeId)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());

    verify(useCase, only()).updateNotice(UpdateNoticeCommand.from(noticeId, request));
  }

  @Test
  @DisplayName("공지사항 수정 시 제목이 없으면 400 반환")
  void shouldReturn400WhenTitleIsMissing() throws Exception {
    // given
    val noticeId = 1L;
    val request = UpdateNoticeRequestFactory.emptyTitle();
    // when
    // then
    mockMvc.perform(put(path, noticeId)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("title은 null이 아니어야 합니다.")));

    verify(useCase, never()).updateNotice(any());
  }

  @Test
  @DisplayName("공지사항 수정 시 내용이 없으면 400 반환")
  void shouldReturn400WhenContentIsMissing() throws Exception {
    // given
    val noticeId = 1L;
    val request = UpdateNoticeRequestFactory.emptyContent();
    // when
    // then
    mockMvc.perform(put(path, noticeId)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("content는 null이 아니어야 합니다.")));

    verify(useCase, never()).updateNotice(any());
  }

  @Test
  @DisplayName("공지사항 수정 시 이미지 리스트가 없으면 400 반환")
  void shouldReturn400WhenImageUrlsIsMissing() throws Exception {
    // given
    val noticeId = 1L;
    val request = UpdateNoticeRequestFactory.emptyImageUrls();
    // when
    // then
    mockMvc.perform(put(path, noticeId)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("imagePaths는 null이 아니어야 합니다.")));

    verify(useCase, never()).updateNotice(any());
  }

  private static class UpdateNoticeRequestFactory {

    public static UpdateNoticeRequest create() {
      return new UpdateNoticeRequest("title", "content", List.of("image"), true);
    }

    public static UpdateNoticeRequest emptyTitle() {
      return new UpdateNoticeRequest(null, "content", List.of("image"), true);
    }

    public static UpdateNoticeRequest emptyContent() {
      return new UpdateNoticeRequest("title", null, List.of("image"), true);
    }

    public static UpdateNoticeRequest emptyImageUrls() {
      return new UpdateNoticeRequest("title", "content", null, true);
    }
  }
}