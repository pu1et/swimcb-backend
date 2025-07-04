package com.project.swimcb.bo.freeswimming.application.port.in;

import com.project.swimcb.bo.freeswimming.domain.BoFreeSwimming;
import java.time.LocalDate;
import java.util.List;
import lombok.NonNull;

public interface FindBoFreeSwimmingUseCase {

  List<BoFreeSwimming> findBoFreeSwimming(
      @NonNull Long swimmingPoolId,
      @NonNull LocalDate yearMonth
  );

}
