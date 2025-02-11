package com.project.swimcb.bo.faq.application.in;

import com.project.swimcb.bo.faq.domain.Faq;
import lombok.NonNull;

public interface RegisterFaqUseCase {

  void registerFaq(@NonNull Faq faq);
}
