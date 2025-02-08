package com.project.swimcb.notice.application.out;

import com.project.swimcb.notice.adapter.in.FindNoticesResponse;
import com.project.swimcb.notice.application.in.FindNoticesUseCase;
import com.project.swimcb.notice.domain.NoticeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindNoticesInteractor implements FindNoticesUseCase {

  private final NoticeRepository noticeRepository;

  @Override
  public Page<FindNoticesResponse> findNotices(@NonNull Pageable pageable) {
    return noticeRepository.findAll(pageable)
        .map(i -> FindNoticesResponse.builder()
            .noticeId(i.getId())
            .title(i.getTitle())
            .createdBy(i.getCreatedBy())
            .createdAt(i.getCreatedAt().toLocalDate())
            .isVisible(i.isVisible())
            .build());
  }
}
