package com.project.swimcb.bo.faq.adapter.in;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.faq.adapter.in.FindFaqsResponse;
import com.project.swimcb.bo.faq.adapter.in.SearchFaqsController;
import com.project.swimcb.config.security.SecurityConfig;
import com.project.swimcb.bo.faq.application.in.SearchFaqsUseCase;
import java.time.LocalDate;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SearchFaqsController.class)
@Import(SecurityConfig.class)
class SearchFaqsControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private SearchFaqsUseCase useCase;

  @Autowired
  private ObjectMapper objectMapper;

  private static final String PATH = "/api/bo/faqs/search";

  @Test
  @DisplayName("FAQ 제목 검색 성공")
  void shouldReturnPagedFaqResponsesWhenFaqsExist() throws Exception {
    // given
    val keyword = "keyword";
    val page = 1;
    val size = 10;
    val pageable = PageRequest.of(page - 1, size);

    val responses = FindFaqsResponseFactory.create();
    val response = new PageImpl<>(responses, pageable, responses.size());

    when(useCase.searchFaqs(anyString(), any())).thenReturn(response);
    // when
    // then
    mockMvc.perform(get(PATH)
            .param("keyword", keyword)
            .param("page", String.valueOf(page))
            .param("size", String.valueOf(size)))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));

    verify(useCase, only()).searchFaqs(keyword, pageable);
  }

  @Test
  @DisplayName("검색어 없이 요청시 400 에러 반환")
  void shouldReturn400WhenKeywordIsMissing() throws Exception {
    // given
    val page = 1;
    val size = 10;
    // when
    // then
    mockMvc.perform(get(PATH)
            .param("page", String.valueOf(page))
            .param("size", String.valueOf(size)))
        .andExpect(status().isBadRequest());

    verify(useCase, never()).searchFaqs(anyString(), any());
  }

  private static class FindFaqsResponseFactory {

    private static List<FindFaqsResponse> create() {
      return List.of(
          FindFaqsResponse.builder()
              .faqId(1L)
              .title("title1")
              .createdBy("createdBy1")
              .createdAt(LocalDate.MIN)
              .isVisible(true)
              .build(),
          FindFaqsResponse.builder()
              .faqId(2L)
              .title("title2")
              .createdBy("createdBy2")
              .createdAt(LocalDate.MIN)
              .isVisible(false)
              .build());
    }
  }
}