package com.project.swimcb.bo.notice.application;

import com.project.swimcb.bo.notice.application.in.CreateNoticeUseCase;
import com.project.swimcb.bo.notice.domain.CreateNoticeCommand;
import com.project.swimcb.bo.notice.domain.Notice;
import com.project.swimcb.bo.notice.domain.NoticeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateNoticeInteractor implements CreateNoticeUseCase {

  private final NoticeRepository noticeRepository;

  @Override
  public void createNotice(@NonNull CreateNoticeCommand command) {
    val notice = Notice.create(command.title(), command.content(), command.isVisible());
    noticeRepository.save(notice);
  }
}
