package com.project.swimcb.notice.application;

import com.project.swimcb.notice.application.in.UpdateNoticeIsVisibleUseCase;
import com.project.swimcb.notice.domain.NoticeRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateNoticeIsVisibleInteractor implements UpdateNoticeIsVisibleUseCase {

  private final NoticeRepository noticeRepository;

  @Override
  public void updateNoticeIsVisible(long noticeId, boolean isVisible) {
    noticeRepository.findById(noticeId)
        .orElseThrow(() -> new NoSuchElementException("공지사항을 찾을 수 없습니다."))
        .updateIsVisible(isVisible);
  }
}
