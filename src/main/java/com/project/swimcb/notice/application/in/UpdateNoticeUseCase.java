package com.project.swimcb.notice.application.in;

import com.project.swimcb.notice.domain.UpdateNoticeCommand;
import lombok.NonNull;

public interface UpdateNoticeUseCase {

  void updateNotice(@NonNull UpdateNoticeCommand command);
}
