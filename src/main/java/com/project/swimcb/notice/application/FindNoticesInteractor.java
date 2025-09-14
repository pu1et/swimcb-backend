package com.project.swimcb.notice.application;

import com.project.swimcb.notice.application.port.in.FindNoticesUseCase;
import com.project.swimcb.notice.domain.Notice;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
class FindNoticesInteractor implements FindNoticesUseCase {

  @Override
  public Page<Notice> findNotices(@NonNull Pageable pageable) {
    return null;
  }

}
