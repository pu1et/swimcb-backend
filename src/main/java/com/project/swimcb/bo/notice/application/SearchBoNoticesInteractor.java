package com.project.swimcb.bo.notice.application;

import com.project.swimcb.bo.notice.adapter.in.FindBoNoticesResponse;
import com.project.swimcb.bo.notice.application.in.SearchBoNoticesUseCase;
import com.project.swimcb.bo.notice.application.out.SearchNoticesDsGateway;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchBoNoticesInteractor implements SearchBoNoticesUseCase {

  private final SearchNoticesDsGateway gateway;

  @Override
  public Page<FindBoNoticesResponse> searchNotices(@NonNull String keyword,
      @NonNull Pageable pageable) {
    return gateway.searchNotices(keyword, pageable)
        .map(FindBoNoticesResponse::from);
  }
}
