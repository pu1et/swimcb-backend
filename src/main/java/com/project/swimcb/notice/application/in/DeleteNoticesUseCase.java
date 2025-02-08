package com.project.swimcb.notice.application.in;

import java.util.List;
import lombok.NonNull;

public interface DeleteNoticesUseCase {

  void deleteAll(@NonNull List<Long> noticeIds);
}
