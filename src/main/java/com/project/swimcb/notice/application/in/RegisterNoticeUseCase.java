package com.project.swimcb.notice.application.in;

import com.project.swimcb.notice.domain.RegisterNoticeCommand;
import lombok.NonNull;

public interface RegisterNoticeUseCase {

  void registerNotice(@NonNull RegisterNoticeCommand command);
}
