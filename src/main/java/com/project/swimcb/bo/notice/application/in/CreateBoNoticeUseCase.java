package com.project.swimcb.bo.notice.application.in;

import com.project.swimcb.bo.notice.domain.CreateBoNoticeCommand;
import lombok.NonNull;

public interface CreateBoNoticeUseCase {

  void createNotice(@NonNull CreateBoNoticeCommand command);
}
