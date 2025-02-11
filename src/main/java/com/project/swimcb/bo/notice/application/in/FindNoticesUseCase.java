package com.project.swimcb.bo.notice.application.in;

import com.project.swimcb.bo.notice.adapter.in.FindNoticesResponse;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindNoticesUseCase {

  Page<FindNoticesResponse> findNotices(@NonNull Pageable pageable);
}
