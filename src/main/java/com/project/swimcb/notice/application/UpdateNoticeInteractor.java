package com.project.swimcb.notice.application;

import com.project.swimcb.notice.application.in.UpdateNoticeUseCase;
import com.project.swimcb.notice.domain.NoticeImage;
import com.project.swimcb.notice.domain.NoticeImageRepository;
import com.project.swimcb.notice.domain.NoticeRepository;
import com.project.swimcb.notice.domain.UpdateNoticeCommand;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateNoticeInteractor implements UpdateNoticeUseCase {

  private final NoticeRepository noticeRepository;
  private final NoticeImageRepository noticeImageRepository;

  @Override
  public void updateNotice(@NonNull UpdateNoticeCommand command) {
    val notice = noticeRepository.findById(command.noticeId())
        .orElseThrow(() -> new NoSuchElementException("공지사항을 찾을 수 없습니다."));
    notice.update(command.title(), command.content(), command.isVisible());

    noticeImageRepository.deleteByNoticeId(notice.getId());

    if (command.imagePaths().isEmpty()) {
      return;
    }

    val noticeImages = command.imagePaths()
        .stream()
        .map(i -> NoticeImage.create(notice, i))
        .toList();

    noticeImageRepository.saveAll(noticeImages);
  }
}
