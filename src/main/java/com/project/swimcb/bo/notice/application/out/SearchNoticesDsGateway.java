package com.project.swimcb.bo.notice.application.out;

import com.project.swimcb.db.entity.NoticeEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchNoticesDsGateway {

  Page<NoticeEntity> searchNotices(@NonNull String keyword, @NonNull Pageable pageable);
}
