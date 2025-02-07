package com.project.swimcb.faq.application.in;

import com.project.swimcb.faq.adapter.in.FindFaqsResponse;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindFaqsUseCase {

  Page<FindFaqsResponse> findFaqs(@NonNull Pageable pageable);
}
