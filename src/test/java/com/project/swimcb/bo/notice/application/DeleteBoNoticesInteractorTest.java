package com.project.swimcb.bo.notice.application;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import com.project.swimcb.db.repository.NoticeRepository;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteBoNoticesInteractorTest {

  @InjectMocks
  private DeleteBoNoticesInteractor interactor;

  @Mock
  private NoticeRepository repository;

  @Test
  @DisplayName("공지사항 ID 리스트 삭제 성공")
  void shouldDeleteNoticesSuccessfully() {
    // given
    val noticeIds = List.of(1L, 2L, 3L);
    // when
    interactor.deleteAll(noticeIds);
    // then
    verify(repository, only()).deleteAllById(noticeIds);
  }

  @Test
  @DisplayName("빈 리스트가 전달되면 삭제하지 않는다.")
  void shouldNotDeleteWhenNoticeIdsListIsEmpty() {
    // given
    final List<Long> emptyNoticeIds = List.of();
    // when
    interactor.deleteAll(emptyNoticeIds);
    // then
    verify(repository, never()).deleteAllById(anyList());
  }
}
