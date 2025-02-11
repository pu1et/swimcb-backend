package com.project.swimcb.bo.faq.application.in;

import com.project.swimcb.bo.faq.adapter.in.FindFaqsResponse;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchFaqsUseCase {

  Page<FindFaqsResponse> searchFaqs(@NonNull String keyword, @NonNull Pageable pageable);
}
