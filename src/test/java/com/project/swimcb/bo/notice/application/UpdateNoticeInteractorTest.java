package com.project.swimcb.bo.notice.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.db.entity.NoticeEntity;
import com.project.swimcb.db.repository.NoticeRepository;
import com.project.swimcb.bo.notice.domain.UpdateNoticeCommand;
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
class UpdateNoticeInteractorTest {

  @InjectMocks
  private UpdateNoticeInteractor interactor;

  @Mock
  private NoticeRepository noticeRepository;

  @Test
  @DisplayName("공지사항이 존재하면 제목, 내용, 공개여부를 수정한다.")
  void shouldUpdateNoticeAndUpdateImages() {
    // given
    val existingNotice = mock(NoticeEntity.class);
    val command = UpdateNoticeCommandFactory.create();

    when(noticeRepository.findById(any())).thenReturn(Optional.of(existingNotice));
    // when
    interactor.updateNotice(command);
    // then
    verify(noticeRepository, only()).findById(command.noticeId());
    verify(existingNotice).update(command.title(), command.content(), command.isVisible());
  }

  @Test
  @DisplayName("공지사항이 존재하지 않으면 예외를 발생시킨다.")
  void shouldThrowExceptionWhenNoticeNotFound() {
    // given
    val command = UpdateNoticeCommandFactory.create();
    when(noticeRepository.findById(any())).thenReturn(Optional.empty());
    // when
    // then
    assertThatThrownBy(() -> interactor.updateNotice(command))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("공지사항을 찾을 수 없습니다.");
  }

  private static class UpdateNoticeCommandFactory {

    private static UpdateNoticeCommand create() {
      return new UpdateNoticeCommand(1L, "title", "content", true);
    }
  }
}
