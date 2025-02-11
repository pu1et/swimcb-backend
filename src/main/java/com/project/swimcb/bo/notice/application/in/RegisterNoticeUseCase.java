package com.project.swimcb.bo.notice.application.in;

import com.project.swimcb.bo.notice.domain.RegisterNoticeCommand;
import lombok.NonNull;

public interface RegisterNoticeUseCase {

  void registerNotice(@NonNull RegisterNoticeCommand command);
}
