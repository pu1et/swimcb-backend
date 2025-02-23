package com.project.swimcb.bo.notice.adapter.in;

import static java.time.LocalDateTime.MIN;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.config.security.SecurityConfig;
import com.project.swimcb.bo.notice.application.in.FindNoticeDetailUseCase;
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

@WebMvcTest(FindNoticeDetailController.class)
@Import(SecurityConfig.class)
class FindNoticeDetailControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private FindNoticeDetailUseCase useCase;

  @Autowired
  private ObjectMapper objectMapper;

  private static final String PATH = "/api/bo/notices/{noticeId}";

  @Test
  @DisplayName("공지사항 ID로 상세 조회 성공")
  void shouldReturnNoticeDetailWhenNoticeExists() throws Exception {
    // given
    val noticeId = 1L;
    val response = FindNoticeDetailResponseFactory.create();

    when(useCase.findDetail(anyLong())).thenReturn(response);
    // when
    // then
    mockMvc.perform(get(PATH, noticeId))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));

    verify(useCase, only()).findDetail(noticeId);
  }

  @Test
  @DisplayName("존재하지 않는 공지사항 조회 시 예외 발생")
  void shouldReturn404WhenNoticeNotFound() throws Exception {
    // given
    val noticeId = 1L;

    when(useCase.findDetail(anyLong())).thenThrow(NoSuchElementException.class);
    // when
    // then
    mockMvc.perform(get(PATH, noticeId))
        .andExpect(status().isNotFound());

    verify(useCase, only()).findDetail(noticeId);
  }

  private static class FindNoticeDetailResponseFactory {

    private static FindNoticeDetailResponse create() {
      return FindNoticeDetailResponse.builder()
          .createdBy("createdBy")
          .title("title")
          .content("content")
          .isVisible(true)
          .createdAt(MIN.toLocalDate())
          .build();
    }
  }
}