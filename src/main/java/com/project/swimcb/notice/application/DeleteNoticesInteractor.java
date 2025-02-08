package com.project.swimcb.notice.application;

import com.project.swimcb.notice.application.in.DeleteNoticesUseCase;
import com.project.swimcb.notice.domain.NoticeRepository;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteNoticesInteractor implements DeleteNoticesUseCase {

  private final NoticeRepository noticeRepository;

  @Override
  public void deleteAll(@NonNull List<Long> noticeIds) {
    if (noticeIds.isEmpty()) {
      return;
    }
    noticeRepository.deleteAllById(noticeIds);
  }
}
