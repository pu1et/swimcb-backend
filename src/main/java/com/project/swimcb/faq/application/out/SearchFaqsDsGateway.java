package com.project.swimcb.faq.application.out;

import com.project.swimcb.faq.domain.Faq;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchFaqsDsGateway {

  Page<Faq> searchFaqs(@NonNull String keyword, @NonNull Pageable pageable);
}
