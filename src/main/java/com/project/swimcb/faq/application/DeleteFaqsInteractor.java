package com.project.swimcb.faq.application;

import com.project.swimcb.faq.application.in.DeleteFaqsUseCase;
import com.project.swimcb.faq.domain.FaqRepository;
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
    faqRepository.deleteAllById(faqIds);
  }
}
