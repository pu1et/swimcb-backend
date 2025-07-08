package com.project.swimcb.bo.freeswimming.application.port.out;

import com.project.swimcb.bo.freeswimming.domain.SetFreeSwimmingClosedCommand;
import java.util.List;
import lombok.NonNull;

public interface SetFreeSwimmingClosedDsGateway {

  boolean existsAllByIdInAndSwimmingPoolId(
      @NonNull List<Long> freeSwimmingDayStatusIds,
      @NonNull Long swimmingPoolId
  );

  void setFreeSwimmingClosed(@NonNull SetFreeSwimmingClosedCommand command);

}
