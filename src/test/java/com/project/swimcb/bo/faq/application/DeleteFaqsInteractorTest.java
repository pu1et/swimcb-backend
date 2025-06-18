package com.project.swimcb.bo.faq.application;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.project.swimcb.db.repository.FaqRepository;
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

  @Test
  @DisplayName("빈 리스트가 전달되면 삭제하지 않는다.")
  void shouldNotDeleteWhenFaqIdsListIsEmpty() {
    // given
    final List<Long> emptyFaqIds = List.of();
    // when
    interactor.deleteAll(emptyFaqIds);
    // then
    verify(faqRepository, never()).deleteAllById(anyList());
  }
}
