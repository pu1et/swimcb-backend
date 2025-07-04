package com.project.swimcb.bo.faq.application;

import com.project.swimcb.bo.faq.application.in.UpdateFaqUseCase;
import com.project.swimcb.db.repository.FaqRepository;
import com.project.swimcb.bo.faq.domain.UpdateFaqCommand;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateFaqInteractor implements UpdateFaqUseCase {

  private final FaqRepository faqRepository;

  @Override
  public void updateFaq(@NonNull UpdateFaqCommand command) {
    faqRepository.findById(command.faqId())
        .orElseThrow(() -> new NoSuchElementException("FAQ를 찾을 수 없습니다."))
        .update(command.title(), command.content(), command.isVisible());
  }
}
