package com.project.swimcb.bo.notice.application;

import com.project.swimcb.bo.notice.application.in.UpdateBoNoticeUseCase;
import com.project.swimcb.db.repository.NoticeRepository;
import com.project.swimcb.bo.notice.domain.UpdateBoNoticeCommand;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateBoNoticeInteractor implements UpdateBoNoticeUseCase {

  private final NoticeRepository noticeRepository;

  @Override
  public void updateNotice(@NonNull UpdateBoNoticeCommand command) {
    val notice = noticeRepository.findById(command.noticeId())
        .orElseThrow(() -> new NoSuchElementException("공지사항을 찾을 수 없습니다."));
    notice.update(command.title(), command.content(), command.isVisible());
  }
}
