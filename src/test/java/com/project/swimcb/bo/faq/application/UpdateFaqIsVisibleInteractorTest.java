package com.project.swimcb.bo.faq.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.db.entity.FaqEntity;
import com.project.swimcb.db.repository.FaqRepository;
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
class UpdateFaqIsVisibleInteractorTest {

  @InjectMocks
  private UpdateFaqIsVisibleInteractor interactor;

  @Mock
  private FaqRepository faqRepository;

  @Test
  @DisplayName("FAQ 수정 성공")
  void shouldUpdateFaqSuccessfully() {
    // given
    val faqId = 1L;
    val isVisible = true;
    val existingFaq = mock(FaqEntity.class);

    when(faqRepository.findById(anyLong())).thenReturn(Optional.of(existingFaq));
    // when
    interactor.updateFaqIsVisible(faqId, isVisible);
    // then
    verify(faqRepository, only()).findById(faqId);
    verify(existingFaq, only()).updateIsVisible(isVisible);
  }

  @Test
  @DisplayName("존재하지 않는 FAQ 수정 시 예외 발생")
  void shouldThrowExceptionWhenFaqNotFound() {
    // given
    val faqId = 1L;
    val isVisible = true;
    when(faqRepository.findById(anyLong())).thenReturn(Optional.empty());
    // when
    // then
    assertThatThrownBy(() -> interactor.updateFaqIsVisible(faqId, isVisible))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("FAQ를 찾을 수 없습니다.");

    verify(faqRepository, only()).findById(faqId);
  }
}
