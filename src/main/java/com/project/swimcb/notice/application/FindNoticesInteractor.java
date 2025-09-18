package com.project.swimcb.notice.application;

import com.project.swimcb.db.repository.NoticeRepository;
import com.project.swimcb.notice.application.port.in.FindNoticesUseCase;
import com.project.swimcb.notice.domain.Notice;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class FindNoticesInteractor implements FindNoticesUseCase {

  private final NoticeRepository noticeRepository;

  @Override
  public Page<Notice> findNotices(@NonNull Pageable pageable) {
    return noticeRepository.findAllBySwimmingPool_IdAndIsVisibleIsTrue(1L, pageable)
        .map(i -> Notice.builder()
            .noticeId(i.getId())
            .title(i.getTitle())
            .content(i.getContent())
            .date(i.getCreatedAt().toLocalDate())
            .build());
  }

}
