package com.project.swimcb.faq.application;

import com.project.swimcb.faq.application.in.UpdateFaqIsVisibleUseCase;
import com.project.swimcb.faq.domain.FaqRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateFaqIsVisibleInteractor implements UpdateFaqIsVisibleUseCase {

  private final FaqRepository faqRepository;

  @Override
  public void updateFaqIsVisible(long faqId, boolean isVisible) {
    faqRepository.findById(faqId)
        .orElseThrow(() -> new NoSuchElementException("FAQ를 찾을 수 없습니다."))
        .updateIsVisible(isVisible);
  }
}
