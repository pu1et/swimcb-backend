package com.project.swimcb.bo.notice.application;

import com.project.swimcb.bo.notice.application.in.CreateBoNoticeUseCase;
import com.project.swimcb.bo.notice.domain.CreateBoNoticeCommand;
import com.project.swimcb.db.entity.NoticeEntity;
import com.project.swimcb.db.repository.NoticeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateBoNoticeInteractor implements CreateBoNoticeUseCase {

  private final NoticeRepository noticeRepository;

  @Override
  public void createNotice(@NonNull CreateBoNoticeCommand command) {
    val notice = NoticeEntity.create(command.title(), command.content(), command.isVisible());
    noticeRepository.save(notice);
  }
}
