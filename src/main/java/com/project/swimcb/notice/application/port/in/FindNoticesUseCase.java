package com.project.swimcb.notice.application.port.in;

import com.project.swimcb.notice.domain.Notice;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindNoticesUseCase {

  Page<Notice> findNotices(@NonNull Pageable pageable);

}
