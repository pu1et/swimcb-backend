package com.project.swimcb.bo.notice.application.in;

import com.project.swimcb.bo.notice.domain.CreateNoticeCommand;
import lombok.NonNull;

public interface CreateNoticeUseCase {

  void createNotice(@NonNull CreateNoticeCommand command);
}
