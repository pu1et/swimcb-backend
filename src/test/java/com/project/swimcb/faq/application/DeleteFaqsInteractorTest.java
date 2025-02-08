package com.project.swimcb.faq.application;

import static org.mockito.Mockito.verify;

import com.project.swimcb.faq.domain.FaqRepository;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteFaqsInteractorTest {

  @InjectMocks
  private DeleteFaqsInteractor interactor;

  @Mock
  private FaqRepository faqRepository;

  @Test
  @DisplayName("파라미터로 전달된 FAQ ID 리스트를 삭제한다.")
  void deleteAll() {
    // given
    val faqIds = List.of(1L, 2L, 3L);
    // when
    interactor.deleteAll(faqIds);
    // then
    verify(faqRepository).deleteAllById(faqIds);
  }
}