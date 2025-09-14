package com.project.swimcb.bo.notice.application.in;

import com.project.swimcb.bo.notice.domain.UpdateBoNoticeCommand;
import lombok.NonNull;

public interface UpdateBoNoticeUseCase {

  void updateNotice(@NonNull UpdateBoNoticeCommand command);
}
