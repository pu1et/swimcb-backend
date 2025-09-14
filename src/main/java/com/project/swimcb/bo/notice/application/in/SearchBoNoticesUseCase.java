package com.project.swimcb.bo.notice.application.in;

import com.project.swimcb.bo.notice.adapter.in.FindBoNoticesResponse;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchBoNoticesUseCase {

  Page<FindBoNoticesResponse> searchNotices(@NonNull String keyword, @NonNull Pageable pageable);
}
