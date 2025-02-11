package com.project.swimcb.bo.notice.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.notice.application.UpdateNoticeInteractor;
import com.project.swimcb.bo.notice.domain.Notice;
import com.project.swimcb.bo.notice.domain.NoticeImage;
import com.project.swimcb.bo.notice.domain.NoticeImageRepository;
import com.project.swimcb.bo.notice.domain.NoticeRepository;
import com.project.swimcb.bo.notice.domain.UpdateNoticeCommand;
import java.util.List;
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

  @Mock
  private NoticeImageRepository noticeImageRepository;

  @Test
  @DisplayName("공지사항이 존재하면 제목, 내용, 공개여부를 수정하고 이미지를 업데이트한다.")
  void shouldUpdateNoticeAndUpdateImages() {
    // given
    val existingNotice = mock(Notice.class);
    val existingNoticeId = 1L;
    val command = UpdateNoticeCommandFactory.create();

    when(noticeRepository.findById(any())).thenReturn(Optional.of(existingNotice));
    when(existingNotice.getId()).thenReturn(existingNoticeId);
    // when
    interactor.updateNotice(command);
    // then
    verify(noticeRepository, only()).findById(command.noticeId());
    verify(existingNotice).update(command.title(), command.content(), command.isVisible());

    verify(noticeImageRepository, times(1)).deleteByNoticeId(existingNotice.getId());
    verify(noticeImageRepository, times(1)).saveAll(assertArg(i -> {
      assertThat(i).hasSize(2);
      assertThat(i).extracting(j -> j.getNotice().getId()).containsOnly(existingNoticeId);
      assertThat(i).extracting(NoticeImage::getPath)
          .containsExactly("image1", "image2");
    }));
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

  @Test
  @DisplayName("새로운 이미지가 없으면 기존 이미지만 삭제하고 추가 저장하지 않는다.")
  void shouldDeleteExistingImagesWithoutSavingNewImages() {
    // given
    val existingNotice = mock(Notice.class);
    val existingNoticeId = 1L;
    val command = UpdateNoticeCommandFactory.createWithNoImage();

    when(noticeRepository.findById(any())).thenReturn(Optional.of(existingNotice));
    when(existingNotice.getId()).thenReturn(existingNoticeId);
    // when
    interactor.updateNotice(command);
    // then
    verify(noticeRepository, only()).findById(existingNoticeId);
    verify(existingNotice).update(command.title(), command.content(), command.isVisible());
    verify(noticeImageRepository, only()).deleteByNoticeId(existingNoticeId);
    verify(noticeImageRepository, never()).saveAll(any());
  }

  private static class UpdateNoticeCommandFactory {

    public static UpdateNoticeCommand create() {
      return new UpdateNoticeCommand(1L, "title", "content", List.of("image1", "image2"), true);
    }

    public static UpdateNoticeCommand createWithNoImage() {
      return new UpdateNoticeCommand(1L, "title", "content", List.of(), true);
    }
  }
}