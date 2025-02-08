package com.project.swimcb.notice.application.in;

import com.project.swimcb.notice.adapter.in.FindNoticesResponse;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindNoticesUseCase {

  Page<FindNoticesResponse> findNotices(@NonNull Pageable pageable);
}
