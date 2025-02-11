package com.project.swimcb.bo.notice.application.in;

import com.project.swimcb.bo.notice.domain.UpdateNoticeCommand;
import lombok.NonNull;

public interface UpdateNoticeUseCase {

  void updateNotice(@NonNull UpdateNoticeCommand command);
}
