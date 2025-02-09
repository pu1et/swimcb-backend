package com.project.swimcb.notice.application.out;

import com.project.swimcb.notice.domain.Notice;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchNoticesDsGateway {

  Page<Notice> searchNotices(@NonNull String keyword, @NonNull Pageable pageable);
}
