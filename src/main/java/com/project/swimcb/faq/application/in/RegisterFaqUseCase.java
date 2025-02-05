package com.project.swimcb.faq.application.in;

import com.project.swimcb.faq.domain.Faq;
import lombok.NonNull;

public interface RegisterFaqUseCase {

  void registerFaq(@NonNull Faq faq);
}
