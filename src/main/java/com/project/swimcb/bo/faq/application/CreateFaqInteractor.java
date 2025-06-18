package com.project.swimcb.bo.faq.application;

import com.project.swimcb.bo.faq.application.in.CreateFaqUseCase;
import com.project.swimcb.db.repository.FaqRepository;
import com.project.swimcb.db.entity.FaqEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateFaqInteractor implements CreateFaqUseCase {

  private final FaqRepository faqRepository;

  @Override
  public void createFaq(@NonNull FaqEntity faq) {
    faqRepository.save(faq);
  }
}
