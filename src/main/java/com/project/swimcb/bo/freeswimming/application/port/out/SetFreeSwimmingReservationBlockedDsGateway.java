package com.project.swimcb.bo.freeswimming.application.port.out;

import com.project.swimcb.bo.freeswimming.domain.SetFreeSwimmingReservationBlockedCommand;
import java.util.List;
import lombok.NonNull;

public interface SetFreeSwimmingReservationBlockedDsGateway {

  boolean existsAllByIdInAndSwimmingPoolId(
      @NonNull List<Long> freeSwimmingDayStatusIds,
      @NonNull Long swimmingPoolId
  );

  void setFreeSwimmingReservationBlocked(@NonNull SetFreeSwimmingReservationBlockedCommand command);

}
