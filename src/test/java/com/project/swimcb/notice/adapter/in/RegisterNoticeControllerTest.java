package com.project.swimcb.notice.adapter.in;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.everyItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.config.security.SecurityConfig;
import com.project.swimcb.notice.application.in.RegisterNoticeUseCase;
import com.project.swimcb.notice.domain.RegisterNoticeCommand;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RegisterNoticeController.class)
@Import(SecurityConfig.class)
class RegisterNoticeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private RegisterNoticeUseCase useCase;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("공지사항 등록 성공")
  void shouldRegisterNoticeSuccessfully() throws Exception {
    // given
    val request = RegisterNoticeRequestFactory.create();
    // when
    // then
    mockMvc.perform(post("/api/notices")
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    verify(useCase, only()).registerNotice(RegisterNoticeCommand.from(request));
  }

  @Test
  @DisplayName("createdBy가 null이면 400을 반환한다.")
  void shouldReturn400WhenCreatedByIsNull() throws Exception {
    // given
    val request = RegisterNoticeRequestFactory.createWithNoCreatedBy();
    // when
    // then
    mockMvc.perform(post("/api/notices")
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("createdBy는 null일 수 없습니다.")));

    verify(useCase, never()).registerNotice(any());
  }

  @Test
  @DisplayName("title이 null이면 400을 반환한다.")
  void shouldReturn400WhenTitleIsNull() throws Exception {
    // given
    val request = RegisterNoticeRequestFactory.createWithNoTitle();
    // when
    // then
    mockMvc.perform(post("/api/notices")
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("title은 null일 수 없습니다.")));

    verify(useCase, never()).registerNotice(any());
  }

  @Test
  @DisplayName("content가 null이면 400을 반환한다.")
  void shouldReturn400WhenContentIsNull() throws Exception {
    // given
    val request = RegisterNoticeRequestFactory.createWithNoContent();
    // when
    // then
    mockMvc.perform(post("/api/notices")
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("content는 null일 수 없습니다.")));

    verify(useCase, never()).registerNotice(any());
  }

  @Test
  @DisplayName("imageUrls가 null이면 400을 반환한다.")
  void shouldReturn400WhenImageUrlIsNull() throws Exception {
    // given
    val request = RegisterNoticeRequestFactory.createWithNoImageUrls();
    // when
    // then
    mockMvc.perform(post("/api/notices")
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("imageUrls는 null일 수 없습니다.")));

    verify(useCase, never()).registerNotice(any());
  }

  private static class RegisterNoticeRequestFactory {

    public static RegisterNoticeRequest create() {
      return RegisterNoticeRequest.builder()
          .createdBy("createdBy")
          .title("title")
          .content("content")
          .imageUrls(List.of("image1", "image2"))
          .isVisible(true)
          .build();
    }

    public static RegisterNoticeRequest createWithNoCreatedBy() {
      return RegisterNoticeRequest.builder()
          .title("title")
          .content("content")
          .imageUrls(List.of("image1", "image2"))
          .isVisible(true)
          .build();
    }

    public static RegisterNoticeRequest createWithNoTitle() {
      return RegisterNoticeRequest.builder()
          .createdBy("createdBy")
          .content("content")
          .imageUrls(List.of("image1", "image2"))
          .isVisible(true)
          .build();
    }

    public static RegisterNoticeRequest createWithNoContent() {
      return RegisterNoticeRequest.builder()
          .createdBy("createdBy")
          .title("title")
          .imageUrls(List.of("image1", "image2"))
          .isVisible(true)
          .build();
    }

    public static RegisterNoticeRequest createWithNoImageUrls() {
      return RegisterNoticeRequest.builder()
          .createdBy("createdBy")
          .title("title")
          .content("content")
          .isVisible(true)
          .build();
    }
  }
}