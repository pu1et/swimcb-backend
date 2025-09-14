package com.project.swimcb.bo.notice.application.in;

import java.util.List;
import lombok.NonNull;

public interface DeleteBoNoticesUseCase {

  void deleteAll(@NonNull List<Long> noticeIds);
}
