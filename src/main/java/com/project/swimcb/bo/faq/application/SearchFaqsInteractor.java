package com.project.swimcb.bo.faq.application;

import com.project.swimcb.bo.faq.adapter.in.FindFaqsResponse;
import com.project.swimcb.bo.faq.application.in.SearchFaqsUseCase;
import com.project.swimcb.bo.faq.application.out.SearchFaqsDsGateway;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchFaqsInteractor implements SearchFaqsUseCase {

  private final SearchFaqsDsGateway gateway;

  @Override
  public Page<FindFaqsResponse> searchFaqs(@NonNull String keyword, @NonNull Pageable pageable) {
    return gateway.searchFaqs(keyword, pageable)
        .map(FindFaqsResponse::from);
  }
}
