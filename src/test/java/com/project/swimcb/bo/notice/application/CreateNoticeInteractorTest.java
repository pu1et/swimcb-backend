package com.project.swimcb.bo.notice.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.notice.domain.CreateNoticeCommand;
import com.project.swimcb.bo.notice.domain.Notice;
import com.project.swimcb.bo.notice.domain.NoticeRepository;
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

  @Test
  @DisplayName("공지사항을 저장한다.")
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
  }
}