package com.project.swimcb.notice.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.notice.domain.Notice;
import com.project.swimcb.notice.domain.NoticeRepository;
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
class UpdateNoticeIsVisibleInteractorTest {

  @InjectMocks
  private UpdateNoticeIsVisibleInteractor interactor;

  @Mock
  private NoticeRepository noticeRepository;

  @Test
  @DisplayName("공지사항 수정 성공")
  void shouldUpdateNoticeSuccessfully() {
    // given
    val noticeId = 1L;
    val isVisible = true;
    val existingNotice = mock(Notice.class);

    when(noticeRepository.findById(anyLong())).thenReturn(Optional.of(existingNotice));
    // when
    interactor.updateNoticeIsVisible(noticeId, isVisible);
    // then
    verify(noticeRepository, only()).findById(noticeId);
    verify(existingNotice, only()).updateIsVisible(isVisible);
  }

  @Test
  @DisplayName("존재하지 않는 공지사항 수정 시 예외 발생")
  void shouldThrowExceptionWhenFaqNotFound() {
    // given
    val noticeId = 1L;
    val isVisible = true;
    when(noticeRepository.findById(anyLong())).thenReturn(Optional.empty());
    // when
    // then
    assertThatThrownBy(() -> interactor.updateNoticeIsVisible(noticeId, isVisible))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("공지사항을 찾을 수 없습니다.");

    verify(noticeRepository, only()).findById(noticeId);
  }
}