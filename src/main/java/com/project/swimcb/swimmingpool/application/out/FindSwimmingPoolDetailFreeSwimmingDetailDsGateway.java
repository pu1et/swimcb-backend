package com.project.swimcb.swimmingpool.application.out;

import com.project.swimcb.swimmingpool.domain.FindSwimmingPoolDetailFreeSwimmingDetailCondition;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimmingDetail;
import lombok.NonNull;

public interface FindSwimmingPoolDetailFreeSwimmingDetailDsGateway {

  SwimmingPoolDetailFreeSwimmingDetail findSwimmingPoolDetailFreeSwimmingDetail(
      @NonNull FindSwimmingPoolDetailFreeSwimmingDetailCondition condition);

}
