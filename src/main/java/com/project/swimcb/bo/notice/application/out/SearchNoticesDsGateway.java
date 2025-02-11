package com.project.swimcb.bo.notice.application.out;

import com.project.swimcb.bo.notice.domain.Notice;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchNoticesDsGateway {

  Page<Notice> searchNotices(@NonNull String keyword, @NonNull Pageable pageable);
}
