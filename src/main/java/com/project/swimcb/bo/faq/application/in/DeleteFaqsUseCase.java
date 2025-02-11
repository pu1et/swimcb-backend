package com.project.swimcb.bo.faq.application.in;

import java.util.List;
import lombok.NonNull;

public interface DeleteFaqsUseCase {

  void deleteAll(@NonNull List<Long> faqIds);
}
