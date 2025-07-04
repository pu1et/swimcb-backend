package com.project.swimcb.bo.freeswimming.application.port.out;

import com.project.swimcb.bo.freeswimming.domain.BoFreeSwimming;
import java.time.LocalDate;
import java.util.List;
import lombok.NonNull;

public interface FindBoFreeSwimmingDsGateway {

  List<BoFreeSwimming> findBoFreeSwimming(
      @NonNull Long swimmingPoolId,
      @NonNull LocalDate yearMonth
  );

}
