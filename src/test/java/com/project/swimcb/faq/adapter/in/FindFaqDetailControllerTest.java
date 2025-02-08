package com.project.swimcb.faq.adapter.in;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.config.security.SecurityConfig;
import com.project.swimcb.faq.application.in.FindFaqDetailUseCase;
import java.util.NoSuchElementException;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FindFaqDetailController.class)
@Import(SecurityConfig.class)
class FindFaqDetailControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private FindFaqDetailUseCase useCase;

  private ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @DisplayName("FAQ ID로 상세 조회 성공")
  void shouldReturnFaqDetailWhenFaqExists() throws Exception {
    // given
    val faqId = 1L;
    val response = FindFaqDetailResponseFactory.create();

    when(useCase.findDetail(anyLong())).thenReturn(response);
    // when
    // then
    mockMvc.perform(get("/api/faqs/{faqId}", faqId))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));

    verify(useCase, only()).findDetail(faqId);
  }

  @Test
  @DisplayName("존재하지 않는 FAQ 조회 시 예외 발생")
  void shouldReturn404WhenFaqNotFound() throws Exception {
    // given
    val faqId = 1L;

    when(useCase.findDetail(anyLong())).thenThrow(NoSuchElementException.class);
    // when
    // then
    mockMvc.perform(get("/api/faqs/{faqId}", faqId))
        .andExpect(status().isNotFound());

    verify(useCase, only()).findDetail(faqId);
  }

  private static class FindFaqDetailResponseFactory {

    private static FindFaqDetailResponse create() {
      return FindFaqDetailResponse.builder()
          .title("title")
          .content("content")
          .isVisible(true)
          .build();
    }
  }
}