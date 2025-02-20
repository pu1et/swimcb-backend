package com.project.swimcb.bo.notice.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.notice.domain.Notice;
import com.project.swimcb.bo.notice.domain.NoticeImageRepository;
import com.project.swimcb.bo.notice.domain.NoticeRepository;
import com.project.swimcb.bo.notice.domain.CreateNoticeCommand;
import com.project.swimcb.bo.notice.domain.TestNoticeFactory;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateNoticeInteractorTest {

  @InjectMocks
  private CreateNoticeInteractor interactor;

  @Mock
  private NoticeRepository noticeRepository;

  @Mock
  private NoticeImageRepository noticeImageRepository;

  @Test
  @DisplayName("공지사항을 저장하면, 공지사항과 이미지가 저장된다.")
  void shouldCreateNooticeAndSaveIamges() {
    // given
    val command = CreateNoticeCommandFactory.createWithImages();
    val notice = Notice.create(command.title(), command.content(), command.isVisible());
    val savedNotice = TestNoticeFactory.create(1L, notice.getTitle(), notice.getContent(),
        notice.isVisible());

    when(noticeRepository.save(any())).thenReturn(savedNotice);
    // when
    interactor.createNotice(command);
    // then
    verify(noticeRepository, only()).save(assertArg(i -> {
      assertThat(i.getTitle()).isEqualTo(notice.getTitle());
      assertThat(i.getContent()).isEqualTo(notice.getContent());
      assertThat(i.isVisible()).isEqualTo(notice.isVisible());
    }));

    verify(noticeImageRepository, only()).saveAll(assertArg(i -> {
      assertThat(i).hasSize(2);
      assertThat(i).extracting(j -> j.getNotice().getId()).containsOnly(savedNotice.getId());
    }));
  }

  @Test
  @DisplayName("공지사항을 저장할 때, 이미지가 없으면 이미지 저장은 호출되지 않는다.")
  void shouldCreateNoticeWithoutSavingImagesIfNoImages() {
    // given
    val command = CreateNoticeCommandFactory.createWithNoImage();
    val notice = Notice.create(command.title(), command.content(), command.isVisible());
    val savedNotice = TestNoticeFactory.create(1L, notice.getTitle(), notice.getContent(),
        notice.isVisible());

    when(noticeRepository.save(any())).thenReturn(savedNotice);
    // when
    interactor.createNotice(command);
    // then
    verify(noticeRepository, only()).save(assertArg(i -> {
      assertThat(i.getTitle()).isEqualTo(notice.getTitle());
      assertThat(i.getContent()).isEqualTo(notice.getContent());
      assertThat(i.isVisible()).isEqualTo(notice.isVisible());
    }));

    verify(noticeImageRepository, never()).saveAll(any());
  }

  private static class CreateNoticeCommandFactory {

    private static CreateNoticeCommand createWithImages() {
      return CreateNoticeCommand.builder()
          .createdBy("createdBy")
          .title("title")
          .content("content")
          .imagePaths(List.of("/notice/1", "/notice/2"))
          .isVisible(true)
          .build();
    }

    private static CreateNoticeCommand createWithNoImage() {
      return CreateNoticeCommand.builder()
          .createdBy("createdBy")
          .title("title")
          .content("content")
          .imagePaths(List.of())
          .isVisible(true)
          .build();
    }
  }
}