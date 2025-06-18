package com.project.swimcb.bo.faq.application;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.faq.adapter.in.FindFaqsResponse;
import com.project.swimcb.db.entity.FaqEntity;
import com.project.swimcb.db.repository.FaqRepository;
import com.project.swimcb.db.entity.TestFaqFactory;
import java.time.LocalDate;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class FindFaqsInteractorTest {

  @InjectMocks
  private FindFaqsInteractor interactor;

  @Mock
  private FaqRepository faqRepository;

  @Test
  @DisplayName("FAQ 목록을 조회하면 페이징된 응답을 반환한다.")
  void shouldReturnPagedFaqResponses() {
    // given
    val faqs = FaqFactory.create();
    val pageable = PageRequest.of(0, 10);
    val faqPage = new PageImpl<>(faqs, pageable, faqs.size());

    when(faqRepository.findAll(any(Pageable.class))).thenReturn(faqPage);

    // when
    val result = interactor.findFaqs(pageable);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getTotalElements()).isEqualTo(2);

    assertThat(result.getContent())
        .extracting(FindFaqsResponse::faqId)
        .containsExactly(1L, 2L);

    assertThat(result.getContent())
        .extracting(FindFaqsResponse::title)
        .containsExactly("title1", "title2");

    assertThat(result.getContent())
        .extracting(FindFaqsResponse::createdBy)
        .containsExactly("createdBy1", "createdBy2");

    assertThat(result.getContent())
        .extracting(FindFaqsResponse::createdAt)
        .containsExactly(LocalDate.of(2025, 1, 1),
            LocalDate.of(2025, 1, 3));

    assertThat(result.getContent())
        .extracting(FindFaqsResponse::isVisible)
        .containsExactly(true, false);

    verify(faqRepository, only()).findAll(pageable);
  }

  @DisplayName("FAQ 목록이 비어있을 경우, 빈 페이지를 반환한다.")
  @Test
  void shouldReturnEmptyPageWhenNoData() {
    // given
    val pageable = PageRequest.of(0, 10);
    final Page<FaqEntity> emptyPage = Page.empty();

    when(faqRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

    // when
    val result = interactor.findFaqs(pageable);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getTotalElements()).isZero();
    assertThat(result.getContent()).isEmpty();

    verify(faqRepository, only()).findAll(pageable);
  }

  private static class FaqFactory {

    private static List<FaqEntity> create() {
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
