package com.project.swimcb.bo.faq.application;

import com.project.swimcb.bo.faq.application.in.DeleteFaqsUseCase;
import com.project.swimcb.bo.faq.domain.FaqRepository;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteFaqsInteractor implements DeleteFaqsUseCase {

  private final FaqRepository faqRepository;

  @Override
  public void deleteAll(@NonNull List<Long> faqIds) {
    if (faqIds.isEmpty()) {
      return;
    }
    faqRepository.deleteAllById(faqIds);
  }
}
