package com.project.swimcb.bo.notice.adapter.in;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.notice.application.in.CreateBoNoticeUseCase;
import com.project.swimcb.bo.notice.domain.CreateBoNoticeCommand;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = CreateBoNoticeController.class)
class CreateBoNoticeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CreateBoNoticeUseCase useCase;

  @Autowired
  private ObjectMapper objectMapper;

  private static final String PATH = "/api/bo/notices";

  @Test
  @DisplayName("공지사항 등록 성공")
  void shouldCreateBoNoticeSuccessfully() throws Exception {
    // given
    val request = CreateNoticeRequestFactory.create();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    verify(useCase, only()).createNotice(CreateBoNoticeCommand.from(request));
  }

  @Test
  @DisplayName("createdBy가 null이면 400을 반환한다.")
  void shouldReturn400WhenCreatedByIsNull() throws Exception {
    // given
    val request = CreateNoticeRequestFactory.createWithNoCreatedBy();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("createdBy는 null일 수 없습니다.")));

    verify(useCase, never()).createNotice(any());
  }

  @Test
  @DisplayName("title이 null이면 400을 반환한다.")
  void shouldReturn400WhenTitleIsNull() throws Exception {
    // given
    val request = CreateNoticeRequestFactory.createWithNoTitle();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("title은 null일 수 없습니다.")));

    verify(useCase, never()).createNotice(any());
  }

  @Test
  @DisplayName("content가 null이면 400을 반환한다.")
  void shouldReturn400WhenContentIsNull() throws Exception {
    // given
    val request = CreateNoticeRequestFactory.createWithNoContent();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("content는 null일 수 없습니다.")));

    verify(useCase, never()).createNotice(any());
  }

  @Test
  @DisplayName("imageUrls가 null이면 400을 반환한다.")
  void shouldReturn400WhenImageUrlIsNull() throws Exception {
    // given
    val request = CreateNoticeRequestFactory.createWithNoImageUrls();
    // when
    // then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("imagePaths는 null일 수 없습니다.")));

    verify(useCase, never()).createNotice(any());
  }

  private static class CreateNoticeRequestFactory {

    private static CreateBoNoticeRequest create() {
      return CreateBoNoticeRequest.builder()
          .createdBy("createdBy")
          .title("title")
          .content("content")
          .imagePaths(List.of("image1", "image2"))
          .isVisible(true)
          .build();
    }

    private static CreateBoNoticeRequest createWithNoCreatedBy() {
      return CreateBoNoticeRequest.builder()
          .title("title")
          .content("content")
          .imagePaths(List.of("image1", "image2"))
          .isVisible(true)
          .build();
    }

    private static CreateBoNoticeRequest createWithNoTitle() {
      return CreateBoNoticeRequest.builder()
          .createdBy("createdBy")
          .content("content")
          .imagePaths(List.of("image1", "image2"))
          .isVisible(true)
          .build();
    }

    private static CreateBoNoticeRequest createWithNoContent() {
      return CreateBoNoticeRequest.builder()
          .createdBy("createdBy")
          .title("title")
          .imagePaths(List.of("image1", "image2"))
          .isVisible(true)
          .build();
    }

    private static CreateBoNoticeRequest createWithNoImageUrls() {
      return CreateBoNoticeRequest.builder()
          .createdBy("createdBy")
          .title("title")
          .content("content")
          .isVisible(true)
          .build();
    }
  }
}
