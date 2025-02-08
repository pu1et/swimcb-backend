package com.project.swimcb.faq.application;

import com.project.swimcb.faq.adapter.in.FindFaqDetailResponse;
import com.project.swimcb.faq.application.in.FindFaqDetailUseCase;
import com.project.swimcb.faq.domain.FaqRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindFaqDetailInteractor implements FindFaqDetailUseCase {

  private final FaqRepository faqRepository;

  @Override
  public FindFaqDetailResponse findDetail(long faqId) {
    return faqRepository.findById(faqId)
        .map(FindFaqDetailResponse::from)
        .orElseThrow(() -> new NoSuchElementException("FAQ가 존재하지 않습니다."));
  }
}
