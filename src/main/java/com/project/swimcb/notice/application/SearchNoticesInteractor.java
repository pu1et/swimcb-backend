package com.project.swimcb.notice.application;

import com.project.swimcb.notice.adapter.in.FindNoticesResponse;
import com.project.swimcb.notice.application.in.SearchNoticesUseCase;
import com.project.swimcb.notice.application.out.SearchNoticesDsGateway;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchNoticesInteractor implements SearchNoticesUseCase {

  private final SearchNoticesDsGateway gateway;

  @Override
  public Page<FindNoticesResponse> searchNotices(@NonNull String keyword,
      @NonNull Pageable pageable) {
    return gateway.searchNotices(keyword, pageable)
        .map(FindNoticesResponse::from);
  }
}
