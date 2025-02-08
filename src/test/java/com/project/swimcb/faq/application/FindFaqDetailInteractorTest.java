package com.project.swimcb.faq.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.faq.adapter.in.FindFaqDetailResponse;
import com.project.swimcb.faq.domain.Faq;
import com.project.swimcb.faq.domain.FaqRepository;
import com.project.swimcb.faq.domain.TestFaqFactory;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindFaqDetailInteractorTest {

  @InjectMocks
  private FindFaqDetailInteractor interactor;

  @Mock
  private FaqRepository faqRepository;

  @Test
  @DisplayName("FAQ ID로 상세 조회 성공")
  void shouldReturnFaqDetailWhenFaqExists() {
    // given
    val faqId = 1L;
    val faq = Optional.of(FaqFactory.create());
    val expectedResponse = FindFaqDetailResponse.from(faq.get());

    when(faqRepository.findById(any())).thenReturn(faq);
    // when
    val response = interactor.findDetail(faqId);
    // then
    assertThat(response).isEqualTo(expectedResponse);
    verify(faqRepository).findById(faqId);
  }

  @Test
  @DisplayName("존재하지 않는 FAQ 조회 시 예외 발생")
  void findById() {
    // given
    val faqId = 1L;
    when(faqRepository.findById(any())).thenReturn(Optional.empty());
    // when
    // then
    assertThatThrownBy(() -> interactor.findDetail(faqId))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("FAQ가 존재하지 않습니다.");

    verify(faqRepository).findById(faqId);
  }

  private static class FaqFactory {

    private static Faq create() {
      return TestFaqFactory.create(1L, "title1", "content1", true,
          LocalDateTime.of(2025, 1, 1, 1, 1, 1), "createdBy1",
          LocalDateTime.of(2025, 1, 2, 1, 1, 1),
          "updatedBy1");
    }
  }
}