package com.project.swimcb.faq.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.faq.adapter.in.FindFaqsResponse;
import com.project.swimcb.faq.application.out.SearchFaqsDsGateway;
import com.project.swimcb.faq.domain.Faq;
import com.project.swimcb.faq.domain.TestFaqFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class SearchFaqsInteractorTest {

  @InjectMocks
  private SearchFaqsInteractor interactor;

  @Mock
  private SearchFaqsDsGateway gateway;

  @Test
  @DisplayName("FAQ 검색시 정상적으로 결과가 반환")
  void shouldReturnPagedFaqResponsesWhenFaqsExist() {
    // given
    val keyword = "keyword";
    val pageable = Pageable.ofSize(10);

    val faqs = FaqFactory.create();
    val faqPage = new PageImpl<>(faqs, pageable, faqs.size());

    when(gateway.searchFaqs(any(), any())).thenReturn(faqPage);
    // when
    val result = interactor.searchFaqs(keyword, pageable);
    // then
    assertThat(result).hasSize(2);
    assertThat(result.getContent()).extracting(FindFaqsResponse::faqId)
        .containsExactly(1L, 2L);

    verify(gateway, only()).searchFaqs(keyword, pageable);
  }

  @Test
  @DisplayName("FAQ 검색시 결과가 없을 때 빈 페이지 반환")
  void shouldReturnEmptyPageWhenNoFaqsExist() {
    // given
    val keyword = "keyword";
    val pageable = Pageable.ofSize(10);

    when(gateway.searchFaqs(any(), any())).thenReturn(Page.empty());
    // when
    val result = interactor.searchFaqs(keyword, pageable);
    // then
    assertThat(result).isEmpty();

    verify(gateway, only()).searchFaqs(keyword, pageable);
  }

  private static class FaqFactory {

    private static List<Faq> create() {
      return List.of(TestFaqFactory.create(1L, "title1", "content1", true,
              LocalDateTime.of(2025, 1, 1, 1, 1, 1), "createdBy1",
              LocalDateTime.of(2025, 1, 2, 1, 1, 1),
              "updatedBy1"),
          TestFaqFactory.create(2L, "title2", "content2", false,
              LocalDateTime.of(2025, 1, 3, 1, 1, 1), "createdBy2",
              LocalDateTime.of(2025, 1, 4, 1, 1, 1), "updatedBy2"));
    }
  }
}