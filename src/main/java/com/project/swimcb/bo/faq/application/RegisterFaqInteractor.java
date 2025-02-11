package com.project.swimcb.bo.faq.application;

import com.project.swimcb.bo.faq.application.in.RegisterFaqUseCase;
import com.project.swimcb.bo.faq.domain.FaqRepository;
import com.project.swimcb.bo.faq.domain.Faq;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterFaqInteractor implements RegisterFaqUseCase {

  private final FaqRepository faqRepository;

  @Override
  public void registerFaq(@NonNull Faq faq) {
    faqRepository.save(faq);
  }
}
