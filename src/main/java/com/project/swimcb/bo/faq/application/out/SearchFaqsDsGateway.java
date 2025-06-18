package com.project.swimcb.bo.faq.application.out;

import com.project.swimcb.db.entity.FaqEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchFaqsDsGateway {

  Page<FaqEntity> searchFaqs(@NonNull String keyword, @NonNull Pageable pageable);
}
