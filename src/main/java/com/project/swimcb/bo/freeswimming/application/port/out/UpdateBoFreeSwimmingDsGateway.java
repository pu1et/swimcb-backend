package com.project.swimcb.bo.freeswimming.application.port.out;

import com.project.swimcb.bo.freeswimming.adapter.in.UpdateBoFreeSwimmingCommand;
import lombok.NonNull;

public interface UpdateBoFreeSwimmingDsGateway {

  void updateFreeSwimming(@NonNull UpdateBoFreeSwimmingCommand command);

  void deleteAllTicketsByFreeSwimmingId(@NonNull Long freeSwimmingId);

}
