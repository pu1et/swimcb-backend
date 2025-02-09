package com.project.swimcb.notice.application;

import com.project.swimcb.notice.application.in.RegisterNoticeUseCase;
import com.project.swimcb.notice.domain.Notice;
import com.project.swimcb.notice.domain.NoticeImage;
import com.project.swimcb.notice.domain.NoticeImageRepository;
import com.project.swimcb.notice.domain.NoticeRepository;
import com.project.swimcb.notice.domain.RegisterNoticeCommand;
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

    val noticeImages = command.imageUrls()
        .stream()
        .map(i -> NoticeImage.create(savedNotice, i))
        .toList();

    if (noticeImages.isEmpty()) {
      return;
    }
    noticeImageRepository.saveAll(noticeImages);
  }
}
