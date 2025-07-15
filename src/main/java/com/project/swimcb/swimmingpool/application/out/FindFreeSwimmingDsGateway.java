package com.project.swimcb.swimmingpool.application.out;

import com.project.swimcb.swimmingpool.domain.FindFreeSwimmingCondition;
import com.project.swimcb.swimmingpool.domain.FreeSwimming;
import java.util.List;
import lombok.NonNull;

public interface FindFreeSwimmingDsGateway {

  List<FreeSwimming> findFreeSwimming(@NonNull FindFreeSwimmingCondition condition);

}
