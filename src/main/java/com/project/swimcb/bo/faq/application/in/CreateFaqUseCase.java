package com.project.swimcb.bo.faq.application.in;

import com.project.swimcb.db.entity.FaqEntity;
import lombok.NonNull;

public interface CreateFaqUseCase {

  void createFaq(@NonNull FaqEntity faq);
}
