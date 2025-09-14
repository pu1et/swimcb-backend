package com.project.swimcb.bo.notice.application;

import com.project.swimcb.db.repository.NoticeRepository;
import com.project.swimcb.bo.notice.application.in.DeleteBoNoticesUseCase;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteBoNoticesInteractor implements DeleteBoNoticesUseCase {

  private final NoticeRepository noticeRepository;

  @Override
  public void deleteAll(@NonNull List<Long> noticeIds) {
    if (noticeIds.isEmpty()) {
      return;
    }
    noticeRepository.deleteAllById(noticeIds);
  }
}
