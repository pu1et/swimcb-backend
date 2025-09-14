package com.project.swimcb.bo.notice.application;

import com.project.swimcb.bo.notice.adapter.in.FindBoNoticesResponse;
import com.project.swimcb.bo.notice.application.in.FindBoNoticesUseCase;
import com.project.swimcb.db.repository.NoticeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindBoNoticesInteractor implements FindBoNoticesUseCase {

  private final NoticeRepository noticeRepository;

  @Override
  public Page<FindBoNoticesResponse> findNotices(
      @NonNull Long swimmingPoolId,
      @NonNull Pageable pageable
  ) {
    return noticeRepository.findAllBySwimmingPool_Id(swimmingPoolId, pageable)
        .map(i -> FindBoNoticesResponse.builder()
            .noticeId(i.getId())
            .title(i.getTitle())
            .createdBy(i.getCreatedBy())
            .createdAt(i.getCreatedAt().toLocalDate())
            .isVisible(i.isVisible())
            .build());
  }

}
