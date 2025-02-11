package com.project.swimcb.bo.notice.application;

import com.project.swimcb.bo.notice.application.in.RegisterNoticeUseCase;
import com.project.swimcb.bo.notice.domain.RegisterNoticeCommand;
import com.project.swimcb.bo.notice.domain.Notice;
import com.project.swimcb.bo.notice.domain.NoticeImage;
import com.project.swimcb.bo.notice.domain.NoticeImageRepository;
import com.project.swimcb.bo.notice.domain.NoticeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterNoticeInteractor implements RegisterNoticeUseCase {

  private final NoticeRepository noticeRepository;
  private final NoticeImageRepository noticeImageRepository;

  @Override
  public void registerNotice(@NonNull RegisterNoticeCommand command) {
    val notice = Notice.create(command.title(), command.content(), command.isVisible());
    val savedNotice = noticeRepository.save(notice);

    val noticeImages = command.imagePaths()
        .stream()
        .map(i -> NoticeImage.create(savedNotice, i))
        .toList();

    if (noticeImages.isEmpty()) {
      return;
    }
    noticeImageRepository.saveAll(noticeImages);
  }
}
