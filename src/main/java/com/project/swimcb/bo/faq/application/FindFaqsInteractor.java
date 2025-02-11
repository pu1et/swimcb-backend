package com.project.swimcb.bo.faq.application;

import com.project.swimcb.bo.faq.application.in.FindFaqsUseCase;
import com.project.swimcb.bo.faq.domain.FaqRepository;
import com.project.swimcb.bo.faq.adapter.in.FindFaqsResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindFaqsInteractor implements FindFaqsUseCase {

  private final FaqRepository faqRepository;

  @Override
  public Page<FindFaqsResponse> findFaqs(@NonNull Pageable pageable) {
    return faqRepository.findAll(pageable)
        .map(i -> FindFaqsResponse.builder()
            .faqId(i.getId())
            .title(i.getTitle())
            .createdBy(i.getCreatedBy())
            .createdAt(i.getCreatedAt().toLocalDate())
            .isVisible(i.isVisible())
            .build());
  }
}
